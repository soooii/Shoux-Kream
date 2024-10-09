package com.shoux_kream.checkout;

import com.shoux_kream.checkout.dto.CheckOutRequestDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.checkout.entity.CheckOut;
import org.mapstruct.Mapper;


//TODO mapper 공동 관리 or 개인 도메인별 분리
//@Mapper
//public interface CheckOutMapper {
//    CheckOut toEntity(CheckOutRequestDto request);
//    CheckOutResponseDto toDto(CheckOut checkOut);
//}


/*

UserMapper mapper = UserMapper.INSTANCE;
    mapper.toCommand(~) // 이렇게 인터페이스에 주입받아서 사용
 */