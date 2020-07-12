package dandan.wendy.community.service;

import dandan.wendy.community.dto.PaginationDTO;
import dandan.wendy.community.dto.QuestionDTO;
import dandan.wendy.community.mapper.QuestionMapper;
import dandan.wendy.community.mapper.UserMapper;
import dandan.wendy.community.model.Question;
import dandan.wendy.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public PaginationDTO List(int page, int size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        int totalPage;

        int totalCount = questionMapper.count();

        System.out.println("totalcount  " +totalCount);

        if(totalCount%size  ==0){
            totalPage = totalCount/size;
        }else{
            totalPage = totalCount/size +1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        int offset = size*(page-1);
        List<Question> questions = questionMapper.List(offset,size);

        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions) {
           User user= userMapper.findById(question.getCreator());

           QuestionDTO questionDTO = new QuestionDTO();
           BeanUtils.copyProperties(question,questionDTO);
           questionDTO.setUser(user);
           questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO List(int userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        int totalPage;

        int totalCount = questionMapper.countByUserId(userId);

        if(totalCount%size ==0){
            totalPage = totalCount/size;
        }else{
            totalPage = totalCount/size +1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);

        int offset = size*(page-1);
        System.out.println("offset  "+offset+"userId  ");
        List<Question> questions = questionMapper.ListByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions) {
            User user= userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }
}
