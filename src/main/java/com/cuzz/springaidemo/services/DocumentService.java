package com.cuzz.springaidemo.services;


import com.cuzz.springaidemo.dao.DocumentDao;
import com.cuzz.springaidemo.models.Result;
import com.cuzz.springaidemo.models.dto.DocumentDTO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class DocumentService {

    @Resource
    DocumentDao documentDao;

    public boolean addDocument(DocumentDTO documentDTO){


        boolean b = documentDao.addDocument(documentDTO);
        return b;
    }

    public boolean deleteDocument(String name){

        documentDao.deleteDocument(name);
        return true;

    }

    public boolean changeDocument(DocumentDTO documentDTO){

        boolean b=documentDao.changeDocument(documentDTO.getId().toString(),documentDTO);
            return b;
    }

    public DocumentDTO getDocumentByName(String name){
        DocumentDTO documentDTO = documentDao.queryDocumentByName(name);
        return documentDTO;
    }

    public PageInfo getPageDocument(int pageNum,int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<DocumentDTO> documentDTOS = documentDao.queryAllDocument();
        return new PageInfo<>(documentDTOS);

    }

    public List<DocumentDTO> getAllDocument(){

        return documentDao.queryAllDocument();

    }


    public List<String> getAllDocumentName(){


        return  documentDao.getAllDocumentName();
    }

}
