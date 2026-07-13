package com.dteema.dteema.dto.mapper;

import com.dteema.dteema.dto.message.MessageDTO;
import com.dteema.dteema.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDTO toDTO(Message message);

    Message toModel(MessageDTO messageDTO);
}
