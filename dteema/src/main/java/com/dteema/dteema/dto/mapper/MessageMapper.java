package com.dteema.dteema.dto.mapper;

import com.dteema.dteema.dto.message.MessageDTO;
import com.dteema.dteema.model.chatting.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageDTO toDTO(Message message);

    Message toModel(MessageDTO messageDTO);
}
