package com.cuzz.springaidemo.controllers;


import com.cuzz.springaidemo.models.Result;
import com.cuzz.springaidemo.models.dto.DocumentDTO;
import com.cuzz.springaidemo.models.dto.RoleDTO;
import com.cuzz.springaidemo.models.dto.RoleKnowledgeDTO;
import com.cuzz.springaidemo.models.dto.RoleKnowledgesDTO;
import com.cuzz.springaidemo.services.RedisVectorService;
import com.cuzz.springaidemo.services.DocumentService;
import com.cuzz.springaidemo.services.RoleService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/config")
public class AiDataController {

    @Resource
    RoleService roleService;
    @Resource
    DocumentService documentService;


    @Resource
    RedisVectorService aiservice;

    @GetMapping("/getDocumentByName")
    @CrossOrigin
    public Result getDocumentByName(@RequestParam("name") String  namexx){
        DocumentDTO documentByName = documentService.getDocumentByName(namexx);
        return Result.ok(documentByName);

    }


    @GetMapping("/getAllDocument")
    @CrossOrigin
    public Result getAllDocument(){

        List<DocumentDTO> allDocument = documentService.getAllDocument();
        return Result.ok(allDocument);

    }
    @GetMapping("/getPageDocument")
    @CrossOrigin
    public Result getPageDocument(@RequestParam(defaultValue="1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize
    ){


        PageInfo pageDocument = documentService.getPageDocument(pageNum, pageSize);
        Result<PageInfo> ok = Result.ok(pageDocument);
        return ok;
    }

    @PostMapping("/addDocument")
    @CrossOrigin
    public Result addDocument(@RequestBody DocumentDTO  documentDTO){
        try {
            // Step 1: 尝试在 MySQL 中添加文档
            boolean mysqlSuccess = documentService.addDocument(documentDTO);
            if (!mysqlSuccess) {
                throw new RuntimeException("MySQL operation failed");
            }

            // Step 2: 尝试在 Redis 中添加文档
            aiservice.addDocument2Store(documentDTO);

            // 如果两个操作都成功，返回成功
            return Result.ok(true);
        } catch (Exception e) {
            // Step 3: 如果 Redis 操作失败，触发补偿机制，撤销 MySQL 操作
            log.error("Transaction failed", e);
            documentService.deleteDocument(documentDTO.getDocumentName()); // 补偿操作：撤销 MySQL 操作
            return Result.error(400,"Transaction failed");
        }
    }


    @DeleteMapping("/deleteDocument")
    @CrossOrigin
    public Result deleteDocument(@RequestParam String name){
        boolean b = documentService.deleteDocument(name);
        aiservice.deleteDocumentByName(name);
        return Result.ok(b);

    }

    @PostMapping("/addRole")
    @CrossOrigin
    public Result addRole(@RequestBody RoleDTO roleDTO){
        roleService.addRole(roleDTO);
        return Result.ok("保存成功");

    }

    @PostMapping("/updateRole")
    @CrossOrigin
    public Result updateRole(@RequestBody RoleDTO roleDTO){
        roleService.addRole(roleDTO);
        return Result.ok("修改成功");

    }

    @GetMapping("/getAllRoles")
    @CrossOrigin
    public Result getAllRoles(){
        List<RoleDTO> allRoleDTO = roleService.getAllRoleDTO();
        return Result.ok(allRoleDTO);

    }


    @DeleteMapping("/DeleteRole")
    @CrossOrigin
    public Result DeleteRole(@RequestParam String name){
        Boolean b = roleService.deleteRole(name);
        return Result.ok(b);

    }


    @GetMapping("/getAllKnowledgeName")
    @CrossOrigin
    public Result getAllKnowledgeName(){

        List<String> allKnowledge= documentService.getAllDocumentName();
        return Result.ok(allKnowledge);

    }



    @PostMapping("/addAllKnowledgeToRole")
    @CrossOrigin
    @Transactional // 添加事务注解
    public Result allKnowledgeToRole(@RequestBody RoleKnowledgesDTO roleKnowledgesDTO) {
        try {
            // 删除知识并添加新的知识到角色
            boolean isSuccess = roleService.deleteAllKnowledgeFromRole(roleKnowledgesDTO.getRoleName());
            if (isSuccess) {
                // 如果删除成功，继续添加新的知识到角色
                long l = roleService.addAllKnowledgeToRole(roleKnowledgesDTO.getKnowledges(), roleKnowledgesDTO.getRoleName());
                return Result.ok("成功数量:" + l);
            } else {
                // 如果删除失败，抛出异常，事务会回滚
                throw new RuntimeException("删除操作失败");
            }
        } catch (Exception e) {
            // 如果发生异常，事务会回滚
            return Result.error(444, "删除出错: " + e.getMessage());
        }
    }


    @GetMapping("/getPageRole")
    @CrossOrigin
    public Result getPageRole(@RequestParam(defaultValue="1") int pageNum,
                               @RequestParam(defaultValue = "10") int pageSize
    ){
        PageInfo pageOrder = roleService.getPageRole(pageNum, pageSize);
        Result<PageInfo> ok = Result.ok(pageOrder);
        return ok;
    }

    @GetMapping("/getAllKnowledgeFromRole")
    @CrossOrigin
    public Result getAllKnowledgeFromRole(@RequestParam String roleName){


        List<RoleKnowledgeDTO> allKnowledgeFromRole = roleService.getAllKnowledgeFromRole(roleName);
        return Result.ok(allKnowledgeFromRole);


    }

    @DeleteMapping("/deleteAllKnowledgeFromRole")
    @CrossOrigin
    public Result deleteAllKnowledgeFromRole(@RequestParam String roleName){


        boolean b = roleService.deleteAllKnowledgeFromRole(roleName);
        return Result.ok(b);


    }

    @DeleteMapping("/deleteKnowledgeFromRole")
    @CrossOrigin
    public Result deleteKnowledgeFromRole(@RequestParam String roleName,@RequestParam String knowledgeName){


        boolean b = roleService.deleteKnowledgeFromRole(roleName, knowledgeName);
        return Result.ok(b);


    }

    @PostMapping("/addKnowledge2Role")
    @CrossOrigin
    public Result addKnowledge2Role(@RequestParam String roleName,@RequestParam String knowledgeName){

        boolean b = roleService.addKnowledge2Role(roleName, knowledgeName);
        return b?Result.ok("添加成功"):Result.error(400,"添加失败");

    }

    @PostMapping("/addDocumentWithFile")
    @CrossOrigin
    public Result handleFileUpload(@RequestParam("name") String name,
                                   @RequestParam("file") MultipartFile file) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return Result.error(400 ,"文件为空，请选择一个文件！");
        }
        String tempUrl;
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 设置文件存储路径
        String uploadDir = "uploads/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs(); // 如果目录不存在，创建目录
        }

        try {
            // 保存文件
            File tempFile = new File(uploadDir + fileName);
            file.transferTo(tempFile);
            tempUrl=tempFile.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(400,"文件上传失败：" + e.getMessage());
        }

        DocumentDTO documentDTO = aiservice.getDocumentDTOByFile(tempUrl,name);
        try {
            // Step 1: 尝试在 MySQL 中添加文档
            boolean mysqlSuccess = documentService.addDocument(documentDTO);
            if (!mysqlSuccess) {
                throw new RuntimeException("MySQL operation failed");
            }

            // Step 2: 尝试在 Redis 中添加文档
            aiservice.addDocument2Store(documentDTO);

            // 如果两个操作都成功，返回成功
            return Result.ok(true);
        } catch (Exception e) {
            // Step 3: 如果 Redis 操作失败，触发补偿机制，撤销 MySQL 操作
            log.error("Transaction failed", e);
            documentService.deleteDocument(documentDTO.getDocumentName()); // 补偿操作：撤销 MySQL 操作
            return Result.error(400,"Transaction failed");
        }




    }

    @PostMapping("/changeRoleContent")
    @CrossOrigin
    public Result changeRoleContent(@RequestBody RoleDTO roleDTO){

        boolean b = roleService.changeRoleContent(roleDTO);
        return b?Result.ok("修改成功"):Result.error(400,"修改失败");


    }

    @PostMapping("/changeDocumentContent")
    @CrossOrigin
    public Result changeDocumentContent(@RequestParam("id") Long id,@RequestParam("name") String name,
                                        @RequestParam("file") MultipartFile file){

        // 检查文件是否为空
        if (file.isEmpty()) {
            return Result.error(400 ,"文件为空，请选择一个文件！");
        }
        String tempUrl;
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 设置文件存储路径
        String uploadDir = "uploads/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs(); // 如果目录不存在，创建目录
        }

        try {
            // 保存文件
            File tempFile = new File(uploadDir + fileName);
            file.transferTo(tempFile);
            tempUrl=tempFile.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(400,"文件上传失败：" + e.getMessage());
        }

        DocumentDTO documentDTO = aiservice.getDocumentDTOByFile(tempUrl,id);
        documentDTO.setDocumentName(name);
        try {
            // Step 1: 尝试在 MySQL 中修改文档
            System.out.println(documentDTO.getContent());
            boolean mysqlSuccess = documentService.changeDocument(documentDTO);
            if (!mysqlSuccess) {
                throw new RuntimeException("MySQL operation failed");
            }

            // Step 2: 尝试在 Redis 中删除文档
            aiservice.deleteDocumentByName(documentDTO.getDocumentName());
            // Step 3: 尝试在 Redis 中新增文档
            aiservice.addDocument2Store(documentDTO);
            // 如果两个操作都成功，返回成功
            return Result.ok(true);
        } catch (Exception e) {
            // Step 3: 如果 Redis 操作失败，触发补偿机制，撤销 MySQL 操作
            log.error("修改失败", e);
            documentService.deleteDocument(documentDTO.getDocumentName()); // 补偿操作：撤销 MySQL 操作
            return Result.error(400,"修改失败");
        }

    }

}
