package com.cuzz.springaidemo.dao;

import com.cuzz.springaidemo.mapper.RoleDTOMapper;
import com.cuzz.springaidemo.mapper.RoleKnowledgeDTOMapper;
import com.cuzz.springaidemo.models.dto.RoleDTO;
import com.cuzz.springaidemo.models.dto.RoleDTOExample;
import com.cuzz.springaidemo.models.dto.RoleKnowledgeDTO;
import com.cuzz.springaidemo.models.dto.RoleKnowledgeDTOExample;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoleDao {

    @Resource
    RoleDTOMapper roleDTOMapper;


    @Resource
    RoleKnowledgeDTOMapper roleKnowledgeDTOMapper;

    Map<String,RoleDTO> cache= new ConcurrentHashMap<>();

    public List<RoleDTO> getPageRole(){
        RoleDTOExample roleDTOExample = new RoleDTOExample();
        List<RoleDTO> roleDTOS = roleDTOMapper.selectByExample(roleDTOExample);
        return  roleDTOS;

    }
    public long batchAddDocumentToRole(List<String> knowledge,String roleName){

        long l = roleKnowledgeDTOMapper.batchAddRoleKnowledge(knowledge,roleName);
        return l;


    }
    public boolean changeRoleContent(RoleDTO roleDTO){
        String roleName = roleDTO.getRoleName();
        if (queryRole(roleName)==null){
            return false;
        }

        cache.remove(roleName);
        RoleDTOExample roleDTOExample = new RoleDTOExample();
        roleDTOExample.createCriteria().andIdEqualTo(roleDTO.getId());

        int i = roleDTOMapper.updateByExample(roleDTO, roleDTOExample);
        System.out.println("尝试修改"+roleDTO);
        return i>0;


    }

    public boolean addRole(RoleDTO roleDTO){

        int insert = roleDTOMapper.insertSelective(roleDTO);

        return insert>0;
    }

    public List<RoleKnowledgeDTO> getAllKnowledgeFromRole(String roleName){
        RoleKnowledgeDTOExample roleKnowledgeDTOExample = new RoleKnowledgeDTOExample();
        roleKnowledgeDTOExample.createCriteria().andRoleNameEqualTo(roleName);

        List<RoleKnowledgeDTO> roleKnowledgeDTOS = roleKnowledgeDTOMapper.selectByExample(roleKnowledgeDTOExample);

       return roleKnowledgeDTOS;



    }


    public boolean deleteKnowledgeFromRole(String roleName,String knowledgeName){

        RoleKnowledgeDTOExample roleKnowledgeDTOExample = new RoleKnowledgeDTOExample();
        roleKnowledgeDTOExample.createCriteria().andRoleNameEqualTo(roleName).andKnowledgeNameEqualTo(knowledgeName);
        int i = roleKnowledgeDTOMapper.deleteByExample(roleKnowledgeDTOExample);
        return i>0;

    }

    public boolean deleteAllKnowledgeFromRole(String roleName){

        RoleKnowledgeDTOExample roleKnowledgeDTOExample = new RoleKnowledgeDTOExample();
        roleKnowledgeDTOExample.createCriteria().andRoleNameEqualTo(roleName);
        int i = roleKnowledgeDTOMapper.deleteByExample(roleKnowledgeDTOExample);
        return i>=0;

    }
    public boolean addKnowledge2Role(String roleName,String knowledgeName){

        RoleKnowledgeDTO roleKnowledgeDTO = new RoleKnowledgeDTO();
        RoleDTO roleDTO = queryRole(roleName);
        if (roleDTO==null){
            return false;
        }
        roleKnowledgeDTO.setRoleName(roleName);
        roleKnowledgeDTO.setKnowledgeName(knowledgeName);

        int i = roleKnowledgeDTOMapper.insertSelective(roleKnowledgeDTO);
        return i>0;

    }

    public RoleDTO queryRole(String name){
        RoleDTO roleDTO = cache.get(name);
        if (roleDTO!=null){
            return roleDTO;
        }

        RoleDTOExample roleDTOExample = new RoleDTOExample();
        roleDTOExample.createCriteria().andRoleNameEqualTo(name);
        List<RoleDTO> roleDTOS = roleDTOMapper.selectByExample(roleDTOExample);
        if (roleDTOS.isEmpty()){
            return null;
        }else {
            cache.put(name,roleDTOS.get(0));
            return  roleDTOS.get(0);

        }

    }

    public Boolean deleteRole(String roleName){
        RoleDTOExample roleDTOExample = new RoleDTOExample();
        roleDTOExample.createCriteria().andRoleNameEqualTo(roleName);
        cache.remove(roleName);
        int i = roleDTOMapper.deleteByExample(roleDTOExample);
        return i>0;
    }

    public List<RoleDTO> queryAllRole(){

        List<RoleDTO> roleDTOS = roleDTOMapper.selectByExample(new RoleDTOExample());
        roleDTOS.forEach(item->{
            System.out.println(item.getRoleName());

        });
        return roleDTOS;
    }

}
