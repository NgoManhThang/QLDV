package com.viettel.api.service.qldv.partner;

import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.PartnerEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.PartnerDto;
import com.viettel.api.dto.qldv.UnionsDto;
import com.viettel.api.repository.qldv.partner.PartnerRepository;
import com.viettel.api.repository.qldv.validate.ValidateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerServiceImpl implements PartnerService {

    Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    ValidateRepository validateRepository;

    @Override
    public Datatable seachPartner(PartnerDto dto) {
        return partnerRepository.seachPartner(dto);
    }

    @Override
    public ResultDto saveData(PartnerDto dto) {
        return partnerRepository.saveData(dto);
    }

    @Override
    public PartnerEntity getDetail(PartnerDto dto) {
        return partnerRepository.getDetail(dto);
    }

    @Override
    public ResultDto delete(PartnerDto dto) {
        ResultDto resultDto;
        resultDto = validateRight(dto, "DELETE");
        if (Constants.RESULT.SUCCESS.equals(resultDto.getKey())){
            resultDto = partnerRepository.delete(dto);
        }
        return resultDto;
    }

    @Override
    public ResultDto validateRight(PartnerDto dto, String action) {
        ResultDto resultDto = new ResultDto();
        resultDto.setKey(Constants.RESULT.SUCCESS);
        if(Constants.VALIDATE.DELETE.equals(action)){
            //If partner exist in unions then no can delete
            UnionsDto unionsDto = new UnionsDto();
            unionsDto.setPartnerId(dto.getPartnerId());
            List<UnionsDto> lst = validateRepository.getPartnerExistInUnions(unionsDto);
            if(lst != null && lst.size() > 0){
                resultDto.setKey(Constants.RESULT.NO_CAN_DELETE);
                return resultDto;
            }
        }
        return resultDto;
    }
}
