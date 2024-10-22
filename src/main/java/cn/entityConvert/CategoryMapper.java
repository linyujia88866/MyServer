package cn.entityConvert;


import cn.dto.CategoryDto;
import cn.entity.Category;
import cn.vo.CategoryVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE
, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    Category toEntity(CategoryDto dto);
    CategoryVo toVo(Category e);
    List<CategoryVo> toVoList(List<Category> e);
}