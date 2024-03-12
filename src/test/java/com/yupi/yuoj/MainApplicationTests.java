package com.yupi.yuoj;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.yuoj.judge.codesandbox.CodeSandBox;
import com.yupi.yuoj.judge.codesandbox.CodeSandBoxFactory;
import com.yupi.yuoj.judge.codesandbox.CodeSandBoxProxy;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yupi.yuoj.model.enums.QuestionSubmitLanguageEnum;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
class MainApplicationTests {
    @Value("${codeSandBox.type:example}")
    private String type;

    @Test
    void test01() {
        CodeSandBox codeSandBox = CodeSandBoxFactory.getCodeSandBox(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest
                .builder()
                .code("public class Main {\n" +
                      "    public static void main(String[] args) {\n" +
                      "        Integer a = Integer.valueOf(args[0]);\n" +
                      "        Integer b = Integer.valueOf(args[1]);\n" +
                      "        System.out.println(\"结果：\" + (a + b));\n" +
                      "    }\n" +
                      "}")
                .language(QuestionSubmitLanguageEnum.JAVA.getValue())
                .inputList(Arrays.asList("1 2", "2 3", "3 4"))
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }
}
