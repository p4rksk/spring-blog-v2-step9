package shop.mtcoding.blog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.mtcoding.blog.user.UserRequest;
import shop.mtcoding.blog.user.UserResponse;

/**
 * 1. 통합테스트 (스프링의 모든 빈을 IOC에 등록하고 테스트 하는 것)
 * 2. 배포 직전 최종 테스트
 */
@AutoConfigureMockMvc // MockMvc를 IOC의 로드
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) //몫으로 가짜 서버 띄우기 (모든 빈 IOC 로드)
public class UserControllerTest {

    private ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mvc;


    @Test
    public void join_test() throws Exception {
        // given (메서드를 호출하기 위해서 필요한 데이터)
        UserRequest.JoinDTO reqDTO = new UserRequest.JoinDTO();
        reqDTO.setUsername("haha");
        reqDTO.setPassword("1234");
        reqDTO.setEmail("haha@nate.com");

        String reqBody = om.writeValueAsString(reqDTO); //제이슨으로 변환
//        System.out.println("reqBody : "+reqBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.post("/join")
                        .content(reqBody) //바디가 있으면 컨텐트 타입도 넣어야 된다.
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String respBody = actions.andReturn().getResponse().getContentAsString();
        int statusCode = actions.andReturn().getResponse().getStatus();
        System.out.println("respBody : "+respBody);
        System.out.println("statusCode : " + statusCode);



        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("haha"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("haha@nate.com"));
        // 달라가 json의 시작 {을 의미한다.
    }


    @Test
    public void join_username_same_fail_test() throws Exception {
        // given
        UserRequest.JoinDTO reqDTO = new UserRequest.JoinDTO();
        reqDTO.setUsername("ssar");
        reqDTO.setPassword("1234");
        reqDTO.setEmail("ssar@nate.com");

        String reqBody = om.writeValueAsString(reqDTO);
        //System.out.println("reqBody : "+reqBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.post("/join")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String respBody = actions.andReturn().getResponse().getContentAsString();
        //int statusCode = actions.andReturn().getResponse().getStatus();
        //System.out.println("respBody : "+respBody);
        //System.out.println("statusCode : "+statusCode);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("중복된 유저네임입니다"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body").isEmpty());
    }

    // {"status":400,"msg":"영문/숫자 2~20자 이내로 작성해주세요 : username","body":null}
    @Test
    public void join_username_valid_fail_test() throws Exception {
        // given
        UserRequest.JoinDTO reqDTO = new UserRequest.JoinDTO();
        reqDTO.setUsername("김완준");
        reqDTO.setPassword("1234");
        reqDTO.setEmail("ssar@nate.com");

        String reqBody = om.writeValueAsString(reqDTO);
        //System.out.println("reqBody : "+reqBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.post("/join")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String respBody = actions.andReturn().getResponse().getContentAsString();
        //int statusCode = actions.andReturn().getResponse().getStatus();
        //System.out.println("respBody : "+respBody);
        //System.out.println("statusCode : "+statusCode);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("영문/숫자 2~20자 이내로 작성해주세요 : username"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body").isEmpty());
    }
}

