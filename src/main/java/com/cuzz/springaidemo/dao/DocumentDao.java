package com.cuzz.springaidemo.dao;

import com.cuzz.springaidemo.mapper.DocumentDTOMapper;
import com.cuzz.springaidemo.models.dto.DocumentDTO;
import com.cuzz.springaidemo.models.dto.DocumentDTOExample;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.Optional;

@Component
public class DocumentDao {

    @Resource
   DocumentDTOMapper documentDTOMapper;


    public List<String> getAllDocumentName(){
        List<String> allDocumentName = documentDTOMapper.getAllDocumentName();
        return allDocumentName;
    }





    public boolean addDocument(DocumentDTO documentDTO){

        int i = documentDTOMapper.insertSelective(documentDTO);

        return i>0;
    }

    public  boolean changeDocument(String documentName,DocumentDTO documentDTO){

        DocumentDTO oldDocument = queryDocumentByName(documentName);
        if (oldDocument==null){
            return false;
        }
        DocumentDTOExample documentDTOExample = new DocumentDTOExample();
        documentDTOExample.createCriteria().andDocumentNameEqualTo(documentName);
        int i = documentDTOMapper.updateByExampleSelective(documentDTO,documentDTOExample);
        return i>0;

    }
    public  boolean changeDocument(Long id,DocumentDTO documentDTO){

        DocumentDTO oldDocument = queryDocumentById(id);
        if (oldDocument==null){
            return false;
        }
        System.out.println("修改");
        int i = documentDTOMapper.updateByPrimaryKeySelective(documentDTO);

        return i>0;

    }
    public DocumentDTO queryDocumentByName(String documentName){

        DocumentDTOExample documentDTOExample = new DocumentDTOExample();

        documentDTOExample.createCriteria().andDocumentNameEqualTo(documentName);

        DocumentDTO documentDTO = new DocumentDTO();
        int i = documentDTOMapper.updateByExample(documentDTO, documentDTOExample);
//        if (documentDTOS.isEmpty()){
//            return null;
//        }else {
//            return  documentDTOS.get(0);
//        }

        return i>0?null:documentDTO;

    }
    public DocumentDTO queryDocumentById(Long id){

        DocumentDTOExample documentDTOExample = new DocumentDTOExample();

        documentDTOExample.createCriteria().andIdEqualTo(Math.toIntExact(id));
        DocumentDTO documentDTO = new DocumentDTO();
        int i = documentDTOMapper.updateByExample(documentDTO, documentDTOExample);
//        if (documentDTOS.isEmpty()){
//            return null;
//        }else {
//            return  documentDTOS.get(0);
//        }

        return i>0?null:documentDTO;

    }
    public boolean deleteDocument(String name){


        DocumentDTOExample documentDTOExample = new DocumentDTOExample();
        DocumentDTOExample.Criteria criteria = documentDTOExample.createCriteria().andDocumentNameEqualTo(name);
        int i = documentDTOMapper.deleteByExample(documentDTOExample);

        return i>0;

    }

    public List<DocumentDTO> queryAllDocument(){

        DocumentDTOExample documentDTOExample = new DocumentDTOExample();
        documentDTOExample.createCriteria();
        List<DocumentDTO> documentDTOS = documentDTOMapper.selectByExample(documentDTOExample);
        return documentDTOS;



    }


}
