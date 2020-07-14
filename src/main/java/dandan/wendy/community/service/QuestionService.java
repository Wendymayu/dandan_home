package dandan.wendy.community.service;

import dandan.wendy.community.dto.PaginationDTO;
import dandan.wendy.community.dto.QuestionDTO;
import dandan.wendy.community.mapper.QuestionMapper;
import dandan.wendy.community.mapper.UserMapper;
import dandan.wendy.community.model.Question;
import dandan.wendy.community.model.QuestionExample;
import dandan.wendy.community.model.User;
import org.apache.ibatis.session.RowBounds;
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

        int totalCount = (int)questionMapper.countByExample(new QuestionExample());

        System.out.println("totalcount  " + totalCount);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }
        if (page < 1) {
            page = 1;
        }
        paginationDTO.setPagination(totalPage, page);
        int offset = size * (page - 1);

        //List<Question> questions = questionMapper.List(offset, size);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());

            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    //查看个人发布的问题信息
    public PaginationDTO List(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        int totalPage;

       // int totalCount = questionMapper.countByUserId(userId);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);


        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        //因为totalPage可能为0，会导致下面offset=-5,出错，所以在下面判断page最小为1
        if (page > totalPage) {
            page = totalPage;
        }

        if (page < 1) {
            page = 1;
        }
        paginationDTO.setPagination(totalPage, page);

        int offset = size * (page - 1);
        System.out.println("offset  " + offset + "userId  ");
        //List<Question> questions = questionMapper.ListByUserId(userId, offset, size);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);

        return questionDTO;
    }


    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        } else {
            //更新
            //question.setGmtModified(question.getGmtCreate());
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());

            questionMapper.updateByExampleSelective(updateQuestion,example);
        }
    }
}
