package com.yupi.yuoj.judge;

import com.yupi.yuoj.model.dto.questionsubmit.JudgeInfo;
import com.yupi.yuoj.model.entity.QuestionSubmit;

/**
 * @author tangzhen
 * @version 1.0
 * @date 2024/3/2 13:19
 */
public interface JudgeService {
    public QuestionSubmit doJudge(long questionSubmitId);
}
