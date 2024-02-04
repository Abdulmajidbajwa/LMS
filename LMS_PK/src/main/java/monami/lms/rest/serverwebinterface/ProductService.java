package monami.lms.rest.serverwebinterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import monami.lms.datadaos.LMSDAO;
import monami.lms.dataentities.KYCQuestion;
import monami.lms.dataentities.Privilege;
import monami.lms.dataentities.Product;
import monami.lms.dataentities.ProductSpecification;
import monami.lms.responceentities.BasicResponce;
import monami.lms.responceentities.ReponceForWebTableData;
import monami.lms.responceentities.ResponceWithMessage;
import monami.lms.response.datadtos.ApplicationUserResponseDTO;
import monami.lms.response.datadtos.KYCQuestionResponseDTO;
import monami.lms.response.datadtos.ListOfPrivilegeResponseDTO;
import monami.lms.response.datadtos.ListOfProductResponseDTOs;
import monami.lms.response.datadtos.PrivilegeResponseDTO;
import monami.lms.response.datadtos.ProductResponseDTO;
import monami.lms.response.datadtos.ProductSpecificationResponseDTO;
import monami.lms.utilities.Utility;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;


@Service
public class ProductService {
	
	Logger LOG = LoggerFactory.getLogger(RoleService.class);
	
	@Autowired 
	private LMSDAO objLMSDAO;
	
	@Autowired
	private Utility utility;
	
	public BasicResponce GetAllProductsWithDTOResponse(@RequestHeader("authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("ProductService.GetAllProductsWithDTOResponse()--Start");
		}
		BasicResponce authResp = null;

		try {
			List<Product> fromDB=objLMSDAO.getAllProductsEager();		
			
			
			List<ProductResponseDTO> toSend= new ArrayList<ProductResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(Product i:fromDB) {

					ProductResponseDTO toAdd=new ProductResponseDTO();
					toAdd.setProductId(i.getId());
					toAdd.setProductName(i.getProductName());
					toAdd.setProductCatagory(i.getProductCatagory());
					
					Set<ProductSpecificationResponseDTO> toSendProductSpecificationResponseDTOSet=new HashSet<ProductSpecificationResponseDTO>();
					for(ProductSpecification productSpecification:i.getProductSpecification()) {
						ProductSpecificationResponseDTO toAddProductSpecificationResponseDTO= new ProductSpecificationResponseDTO();
						
						toAddProductSpecificationResponseDTO.setName(productSpecification.getProductSpecificationAssumption().getName());
						toAddProductSpecificationResponseDTO.setValue(productSpecification.getAssumptionValue());
						
						toSendProductSpecificationResponseDTOSet.add(toAddProductSpecificationResponseDTO);
					}
					toAdd.setProductSpecification(toSendProductSpecificationResponseDTOSet);
					
					
					Set<KYCQuestionResponseDTO> toSendKycQuestionSet=new HashSet<KYCQuestionResponseDTO>();
					for(KYCQuestion kycQuestion:i.getQuestions()) {
						KYCQuestionResponseDTO toAddKycQuestionDTO= new KYCQuestionResponseDTO();
						
						toAddKycQuestionDTO.setQuestionId(kycQuestion.getId());
						toAddKycQuestionDTO.setQuestion(kycQuestion.getQuestionToAsk());
						toAddKycQuestionDTO.setQuestionCategory(kycQuestion.getCatagory());
						toAddKycQuestionDTO.setAnswerType(kycQuestion.getAnswerType());
						
						toSendKycQuestionSet.add(toAddKycQuestionDTO);
					}
					toAdd.setQuestions(toSendKycQuestionSet);
					
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfProductResponseDTOs(toSend);
				authResp.setRequested_Action(true);
				return authResp;
			}else{
				ResponceWithMessage toReturn = new ResponceWithMessage(false, "No Record Found");
				return toReturn;
			}
			
		} catch (Exception ex) {
			authResp=new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("ProductService.GetAllProductsWithDTOResponse()--End");
			}
		}

	}
	
}
