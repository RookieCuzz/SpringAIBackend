package com.cuzz.springaidemo.services;


import com.cuzz.springaidemo.dao.RoleDao;
import com.cuzz.springaidemo.models.dto.RoleDTO;
import com.cuzz.springaidemo.models.dto.RoleKnowledgeDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Resource
    RoleDao roleDao;



    public boolean addRole(RoleDTO roleDTO){
        return roleDao.addRole(roleDTO);
    }


    public long addAllKnowledgeToRole(List<String> knowledge,String roleName){

        long l = roleDao.batchAddDocumentToRole(knowledge, roleName);
        return l;

    }
    public RoleDTO getRoleDTO(String name){
        RoleDTO roleDTO = roleDao.queryRole(name);
        return roleDTO;
    }

    public boolean changeRoleContent(RoleDTO roleDTO){
        return  roleDao.changeRoleContent(roleDTO);
    }

    public List<RoleKnowledgeDTO> getAllKnowledgeFromRole(String roleName){
        List<RoleKnowledgeDTO> allKnowledgeFromRole = roleDao.getAllKnowledgeFromRole(roleName);
        return allKnowledgeFromRole;


    }
    public boolean deleteKnowledgeFromRole(String roleName,String knowledgeName){


        return  roleDao.deleteKnowledgeFromRole(roleName,knowledgeName);


    }

    public PageInfo getPageRole(int pageNum,int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<RoleDTO> pageRole = roleDao.getPageRole();
        return new PageInfo<>(pageRole);
    }

    public boolean deleteAllKnowledgeFromRole(String roleName){


        return  roleDao.deleteAllKnowledgeFromRole(roleName);


    }

    public List<RoleDTO> getAllRoleDTO(){
        return roleDao.queryAllRole();
    }

    public boolean addKnowledge2Role(String roleName,String knowledgeName){


        boolean b = roleDao.addKnowledge2Role(roleName, knowledgeName);
        return b;

    }

    public Boolean deleteRole(String roleName){



        return roleDao.deleteRole(roleName);

    }

}
