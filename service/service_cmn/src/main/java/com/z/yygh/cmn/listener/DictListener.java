package com.z.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.z.yygh.cmn.mapper.DictMapper;
import com.z.yygh.model.cmn.Dict;
import com.z.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author z
 * @date 2021/8/19
 * @apiNote
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {
    @Autowired
    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);
        dictMapper.insert(dict);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
