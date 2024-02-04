package monami.lms.rest.serverwebinterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;

import monami.lms.datadaos.GeneralLedgerAccountDAO;
import monami.lms.datadaos.LMSDAO;
import monami.lms.dataentities.ApplicationUsers;
import monami.lms.dataentities.DisbursedLoans;
import monami.lms.dataentities.GlForFedLiabilityAccount;
import monami.lms.dataentities.GlForInternalLoanBook;
import monami.lms.dataentities.GlForJazzCashWalletAccount;
import monami.lms.dataentities.GlForLatePaymentFeeIncomeAccount;
import monami.lms.dataentities.GlForLoanReceivableAccount;
import monami.lms.dataentities.GlForOneLoadWalletAccount;
import monami.lms.dataentities.GlForPayableAccount;
import monami.lms.dataentities.GlForServiceFeeIncomeAccount;
import monami.lms.dataentities.GlForSimSimWalletAccount;
import monami.lms.dataentities.LoanApplication;
import monami.lms.dataentities.Preferences;
import monami.lms.dataentities.ProductSpecification;
import monami.lms.responceentities.ResponceWithMessage;
import monami.lms.response.datadtos.ApplicationUserResponseDTO;
import monami.lms.response.datadtos.CustomerResponseDTO;
import monami.lms.response.datadtos.DisbursedLoanResponseDTO;
import monami.lms.response.datadtos.GeneralLedgerResponseDTO;
import monami.lms.response.datadtos.LoanApplicationsResponseDTO;
import monami.lms.response.datadtos.ProductResponseDTO;
import monami.lms.utilities.Constants;
import monami.lms.utilities.LoanApplicationStatus;
import monami.lms.utilities.Utility;

@Service
public class GeneralLedgerService {

	@Autowired
	private GeneralLedgerAccountDAO generalLedgerDao;
	
	@Autowired 
	private LMSDAO objLMSDAO;
	
	@Autowired
	private Utility utility;

	Logger LOG = LoggerFactory.getLogger(ApplicationUserService.class);
	
	
	/*public ResponceWithMessage recordDisbursementFinancialCombinedEntryToGl(int glId, String glDescription, int loanId, String transactionId, long transactionAmount, boolean isDebitTransaction, ApplicationUsers loggedInUser) {


		ResponceWithMessage obj = null;
		try {

			int operationResult=0;
			if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForLoanReceivableAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_SERVICE_FEE_ACCOUNT){
				
				operationResult=generalLedgerDao.financialEntryForLoanReceivableAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
					if(isDebitTransaction){
						isDebitTransaction=false;
					}else{
						isDebitTransaction=true;
					}
					
				operationResult=generalLedgerDao.financialEntryForServiceFeeIncomeAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_LATE_PAYMENT_FEE_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForLoanReceivableAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
				
				if(isDebitTransaction){
					isDebitTransaction=false;
				}else{
					isDebitTransaction=true;
				}
				operationResult=generalLedgerDao.financialEntryForLatePaymentFeeIncomeAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_FED_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForLoanReceivableAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
				
				if(isDebitTransaction){
					isDebitTransaction=false;
				}else{
					isDebitTransaction=true;
				}
				operationResult=generalLedgerDao.financialEntryForFEDLiabilityAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForLoanReceivableAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
				
				if(isDebitTransaction){
					isDebitTransaction=false;
				}else{
					isDebitTransaction=true;
				}
				
				operationResult=generalLedgerDao.financialEntryForInternalLoanBookGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
				operationResult=generalLedgerDao.financialEntryForOneLoadWalletAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_SIMSIM_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForLoanReceivableAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
				
				if(isDebitTransaction){
					isDebitTransaction=false;
				}else{
					isDebitTransaction=true;
				}

				operationResult=generalLedgerDao.financialEntryForInternalLoanBookGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
				operationResult=generalLedgerDao.financialEntryForSimSimWalletAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_JAZZCASH_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForLoanReceivableAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
				if(isDebitTransaction){
					isDebitTransaction=false;
				}else{
					isDebitTransaction=true;
				}
				
				operationResult=generalLedgerDao.financialEntryForInternalLoanBookGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
				operationResult=generalLedgerDao.financialEntryForJazzCashWalletAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else{
				if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_PAYABLE_ACCOUNT){
					
					if(isDebitTransaction){
						isDebitTransaction=false;
					}else{
						isDebitTransaction=true;
					}
					
					operationResult=generalLedgerDao.financialEntryForPayableAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
				}
			}
			
			
					if (operationResult > 0) {
						obj = new ResponceWithMessage(true, "Financial Entry for Gl Account:"+glDescription+" Added successfully.");
					} else {
						obj = new ResponceWithMessage(false, "Error while adding Financial Entry");
					}

		} catch (Exception e) {
			LOG.error("Error",e);
		}
		return obj;
	}*/
	
	
	
	public ResponceWithMessage recordFinancialEntryToGlIndividually(int glId, String glDescription, int loanId, String transactionId, long transactionAmount, boolean isDebitTransaction, ApplicationUsers loggedInUser) {

		ResponceWithMessage obj = null;
		try {
			
			int operationResult=0;
			if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForLoanReceivableAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_SERVICE_FEE_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForServiceFeeIncomeAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_LATE_PAYMENT_FEE_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForLatePaymentFeeIncomeAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_FED_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForFEDLiabilityAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForOneLoadWalletAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_SIMSIM_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForSimSimWalletAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_JAZZCASH_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForJazzCashWalletAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_PAYABLE_ACCOUNT){
				operationResult=generalLedgerDao.financialEntryForPayableAccountGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
			}else{
				if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_INTERNAL_LOAN_BOOK_ACCOUNT){
					operationResult=generalLedgerDao.financialEntryForInternalLoanBookGL(glId,glDescription,loanId,transactionId,transactionAmount,isDebitTransaction,loggedInUser);
				}
			}

			if (operationResult > 0) {
						obj = new ResponceWithMessage(true, "Financial Entry for Gl Account:"+glDescription+" Added successfully.");
					} else {
						obj = new ResponceWithMessage(false, "Error while adding Financial Entry");
					}

		} catch (Exception e) {
			LOG.error("Error",e);
		}
		return obj;
	}
	
	
	public List<GeneralLedgerResponseDTO> getGeneralLedger(int glId) throws Exception {
		List<GeneralLedgerResponseDTO> generalLedgerDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		try {
			if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT){
				generalLedgerDTOList=fromLoanReceivableToDtoConverter(generalLedgerDao.getLoanReceivableAccountGL());
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_SERVICE_FEE_ACCOUNT){
				generalLedgerDTOList=fromServiceFeeToDtoConverter(generalLedgerDao.getServiceFeeIncomeAccountGL());
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_LATE_PAYMENT_FEE_ACCOUNT){
				generalLedgerDTOList=fromLatePaymentFeeToDtoConverter(generalLedgerDao.getLatePaymentFeeIncomeAccountGL());
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_FED_ACCOUNT){
				generalLedgerDTOList=fromFEDToDtoConverter(generalLedgerDao.getFEDLiabilityAccountGL());
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT){
				generalLedgerDTOList=fromOneLoadWalletToDtoConverter(generalLedgerDao.getOneLoadWalletAccountGL());
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_SIMSIM_ACCOUNT){
				generalLedgerDTOList=fromSimSimWalletFeeToDtoConverter(generalLedgerDao.getSimSimWalletAccountGL());
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_JAZZCASH_ACCOUNT){
				generalLedgerDTOList=fromJazzCashWalletFeeToDtoConverter(generalLedgerDao.getJazzCashWalletAccountGL());
			}else if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_PAYABLE_ACCOUNT){
				generalLedgerDTOList=fromPayableAccountToDtoConverter(generalLedgerDao.getPayableAccountGL());
			}else{
				if(glId==Constants.GENERAL_LEDGER_ENTRY_FOR_INTERNAL_LOAN_BOOK_ACCOUNT){
					generalLedgerDTOList=fromInternalBookToDtoConverter(generalLedgerDao.getInternalLoanBookGL());
				}
			}
			
			return generalLedgerDTOList;

		} catch (Exception e) {
			LOG.error("Exception Occurred. Exception is: "+e.getMessage());
			throw e;
		}
	}
	
	
	public List<GeneralLedgerResponseDTO> fromLoanReceivableToDtoConverter(List<GlForLoanReceivableAccount> objList) throws Exception{
		List<GeneralLedgerResponseDTO> gLDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		for(int i=0; i< objList.size();i++){
			GeneralLedgerResponseDTO gLDTO=new GeneralLedgerResponseDTO();
			
			gLDTO.setIdGl(objList.get(i).getIdGl());
			gLDTO.setGlDescription(objList.get(i).getGlDescription());
			
			gLDTO.setDisbursedLoanDTO(getDisbursedLoanDTOFromDisbursedLoan(objList.get(i).getDisbursedLoans()));
			
			gLDTO.setTxnId(objList.get(i).getTxnId());
			
				if(objList.get(i).getAmountCredit()!=null){
					gLDTO.setAmount(objList.get(i).getAmountCredit());
					gLDTO.setDebitTransaction(false);
				}else{
					gLDTO.setAmount(objList.get(i).getAmountDebit());
					gLDTO.setDebitTransaction(true);
				}
			
			if(objList!=null && objList.size()>0 && objList.get(i).getCreatedAt()!=null)
				gLDTO.setCreatedAt(utility.getDateFromTimeStamp(objList.get(i).getCreatedAt().toString()));
			
			if(objList!=null && objList.size()>0 && objList.get(i).getCreatedBy()!=null)
				gLDTO.setCreatedBy(new ApplicationUserResponseDTO(objList.get(i).getCreatedBy().getUserId(), objList.get(i).getCreatedBy().getDisplayName()));
				
			gLDTO.setRequested_Action(true);
			
			gLDTOList.add(gLDTO);
		}
		return gLDTOList;
	}
	
	public List<GeneralLedgerResponseDTO> fromLoanReceivableToDtoConverterForLoanSummary(List<GlForLoanReceivableAccount> objList) throws Exception{
		List<GeneralLedgerResponseDTO> gLDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		for(int i=0; i< objList.size();i++){
			GeneralLedgerResponseDTO gLDTO=new GeneralLedgerResponseDTO();
			
			gLDTO.setIdGl(objList.get(i).getIdGl());
			gLDTO.setGlDescription(objList.get(i).getGlDescription());
			
			gLDTO.setDisbursedLoanDTO(getDisbursedLoanDTOFromDisbursedLoan(objList.get(i).getDisbursedLoans()));
			
			gLDTO.setTxnId(objList.get(i).getTxnId());
			
				if(objList.get(i).getAmountCredit()!=null){
					gLDTO.setAmount(objList.get(i).getAmountCredit());
					gLDTO.setDebitTransaction(true);
				}else{
					gLDTO.setAmount(objList.get(i).getAmountDebit());
					gLDTO.setDebitTransaction(false);
				}
			
			if(objList!=null && objList.size()>0 && objList.get(i).getCreatedAt()!=null)
				gLDTO.setCreatedAt(utility.getDateFromTimeStamp(objList.get(i).getCreatedAt().toString()));
			
			if(objList!=null && objList.size()>0 && objList.get(i).getCreatedBy()!=null)
				gLDTO.setCreatedBy(new ApplicationUserResponseDTO(objList.get(i).getCreatedBy().getUserId(), objList.get(i).getCreatedBy().getDisplayName()));
				
			gLDTO.setRequested_Action(true);
			
			gLDTOList.add(gLDTO);
		}
		return gLDTOList;
	}
	
	public List<GeneralLedgerResponseDTO> fromServiceFeeToDtoConverter(List<GlForServiceFeeIncomeAccount> objList) throws Exception{
		List<GeneralLedgerResponseDTO> gLDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		for(int i=0; i< objList.size();i++){
			GeneralLedgerResponseDTO gLDTO=new GeneralLedgerResponseDTO();
			
			gLDTO.setIdGl(objList.get(i).getIdGl());
			gLDTO.setGlDescription(objList.get(i).getGlDescription());
			gLDTO.setDisbursedLoanDTO(getDisbursedLoanDTOFromDisbursedLoan(objList.get(i).getDisbursedLoans()));
			gLDTO.setTxnId(objList.get(i).getTxnId());
			
				if(objList.get(i).getAmountCredit()!=null){
					gLDTO.setAmount(objList.get(i).getAmountCredit());
					gLDTO.setDebitTransaction(false);
				}else{
					gLDTO.setAmount(objList.get(i).getAmountDebit());
					gLDTO.setDebitTransaction(true);
				}
				
				
				if (objList!=null && objList.size()>0 && objList.get(i).getCreatedAt()!=null)
					gLDTO.setCreatedAt(utility.getDateFromTimeStamp(objList.get(i).getCreatedAt().toString()));
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedBy()!=null)
					gLDTO.setCreatedBy(new ApplicationUserResponseDTO(objList.get(i).getCreatedBy().getUserId(), objList.get(i).getCreatedBy().getDisplayName()));
					
				gLDTO.setRequested_Action(true);
			gLDTOList.add(gLDTO);
		}
		
		
		return gLDTOList;
	}
	
	
	public List<GeneralLedgerResponseDTO> fromFEDToDtoConverter(List<GlForFedLiabilityAccount> objList) throws Exception{
		List<GeneralLedgerResponseDTO> gLDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		for(int i=0; i< objList.size();i++){
			GeneralLedgerResponseDTO gLDTO=new GeneralLedgerResponseDTO();
			
			gLDTO.setIdGl(objList.get(i).getIdGl());
			gLDTO.setGlDescription(objList.get(i).getGlDescription());
			gLDTO.setDisbursedLoanDTO(getDisbursedLoanDTOFromDisbursedLoan(objList.get(i).getDisbursedLoans()));
			gLDTO.setTxnId(objList.get(i).getTxnId());
			
				if(objList.get(i).getAmountCredit()!=null){
					gLDTO.setAmount(objList.get(i).getAmountCredit());
					gLDTO.setDebitTransaction(false);
				}else{
					gLDTO.setAmount(objList.get(i).getAmountDebit());
					gLDTO.setDebitTransaction(true);
				}
				
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedAt()!=null)
					gLDTO.setCreatedAt(utility.getDateFromTimeStamp(objList.get(i).getCreatedAt().toString()));
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedBy()!=null)
					gLDTO.setCreatedBy(new ApplicationUserResponseDTO(objList.get(i).getCreatedBy().getUserId(), objList.get(i).getCreatedBy().getDisplayName()));
					
				
				gLDTO.setRequested_Action(true);
			gLDTOList.add(gLDTO);
		}
		
		
		return gLDTOList;
	}
	
	
	public List<GeneralLedgerResponseDTO> fromInternalBookToDtoConverter(List<GlForInternalLoanBook> objList) throws Exception{
		List<GeneralLedgerResponseDTO> gLDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		for(int i=0; i< objList.size();i++){
			GeneralLedgerResponseDTO gLDTO=new GeneralLedgerResponseDTO();
			
			gLDTO.setIdGl(objList.get(i).getIdGl());
			gLDTO.setGlDescription(objList.get(i).getGlDescription());
			gLDTO.setDisbursedLoanDTO(getDisbursedLoanDTOFromDisbursedLoan(objList.get(i).getDisbursedLoans()));
			gLDTO.setTxnId(objList.get(i).getTxnId());
			
				if(objList.get(i).getAmountCredit()!=null){
					gLDTO.setAmount(objList.get(i).getAmountCredit());
					gLDTO.setDebitTransaction(false);
				}else{
					gLDTO.setAmount(objList.get(i).getAmountDebit());
					gLDTO.setDebitTransaction(true);
				}
				
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedAt()!=null)
					gLDTO.setCreatedAt(utility.getDateFromTimeStamp(objList.get(i).getCreatedAt().toString()));
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedBy()!=null)
					gLDTO.setCreatedBy(new ApplicationUserResponseDTO(objList.get(i).getCreatedBy().getUserId(), objList.get(i).getCreatedBy().getDisplayName()));
					
				gLDTO.setRequested_Action(true);
			gLDTOList.add(gLDTO);
		}
		
		
		return gLDTOList;
	}
	
	public List<GeneralLedgerResponseDTO> fromLatePaymentFeeToDtoConverter(List<GlForLatePaymentFeeIncomeAccount> objList) throws Exception{
		List<GeneralLedgerResponseDTO> gLDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		for(int i=0; i< objList.size();i++){
			GeneralLedgerResponseDTO gLDTO=new GeneralLedgerResponseDTO();
			
			gLDTO.setIdGl(objList.get(i).getIdGl());
			gLDTO.setGlDescription(objList.get(i).getGlDescription());
			gLDTO.setDisbursedLoanDTO(getDisbursedLoanDTOFromDisbursedLoan(objList.get(i).getDisbursedLoans()));
			gLDTO.setTxnId(objList.get(i).getTxnId());
			
				if(objList.get(i).getAmountCredit()!=null){
					gLDTO.setAmount(objList.get(i).getAmountCredit());
					gLDTO.setDebitTransaction(false);
				}else{
					gLDTO.setAmount(objList.get(i).getAmountDebit());
					gLDTO.setDebitTransaction(true);
				}
				
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedAt()!=null)
					gLDTO.setCreatedAt(utility.getDateFromTimeStamp(objList.get(i).getCreatedAt().toString()));
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedBy()!=null)
					gLDTO.setCreatedBy(new ApplicationUserResponseDTO(objList.get(i).getCreatedBy().getUserId(), objList.get(i).getCreatedBy().getDisplayName()));
					
				gLDTO.setRequested_Action(true);
			gLDTOList.add(gLDTO);
		}
		
		
		return gLDTOList;
	}
	
	
	
	public List<GeneralLedgerResponseDTO> fromOneLoadWalletToDtoConverter(List<GlForOneLoadWalletAccount> objList) throws Exception{
		List<GeneralLedgerResponseDTO> gLDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		for(int i=0; i< objList.size();i++){
			GeneralLedgerResponseDTO gLDTO=new GeneralLedgerResponseDTO();
			
			gLDTO.setIdGl(objList.get(i).getIdGl());
			gLDTO.setGlDescription(objList.get(i).getGlDescription());
			gLDTO.setDisbursedLoanDTO(getDisbursedLoanDTOFromDisbursedLoan(objList.get(i).getDisbursedLoans()));
			gLDTO.setTxnId(objList.get(i).getTxnId());
			
				if(objList.get(i).getAmountCredit()!=null){
					gLDTO.setAmount(objList.get(i).getAmountCredit());
					gLDTO.setDebitTransaction(false);
				}else{
					gLDTO.setAmount(objList.get(i).getAmountDebit());
					gLDTO.setDebitTransaction(true);
				}
				
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedAt()!=null)
					gLDTO.setCreatedAt(utility.getDateFromTimeStamp(objList.get(i).getCreatedAt().toString()));
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedBy()!=null)
					gLDTO.setCreatedBy(new ApplicationUserResponseDTO(objList.get(i).getCreatedBy().getUserId(), objList.get(i).getCreatedBy().getDisplayName()));
					
				gLDTO.setRequested_Action(true);
			gLDTOList.add(gLDTO);
		}
		
		
		return gLDTOList;
	}
	
	
	public List<GeneralLedgerResponseDTO> fromJazzCashWalletFeeToDtoConverter(List<GlForJazzCashWalletAccount> objList) throws Exception{
		List<GeneralLedgerResponseDTO> gLDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		for(int i=0; i< objList.size();i++){
			GeneralLedgerResponseDTO gLDTO=new GeneralLedgerResponseDTO();
			
			gLDTO.setIdGl(objList.get(i).getIdGl());
			gLDTO.setGlDescription(objList.get(i).getGlDescription());
			gLDTO.setDisbursedLoanDTO(getDisbursedLoanDTOFromDisbursedLoan(objList.get(i).getDisbursedLoans()));
			gLDTO.setTxnId(objList.get(i).getTxnId());
			
				if(objList.get(i).getAmountCredit()!=null){
					gLDTO.setAmount(objList.get(i).getAmountCredit());
					gLDTO.setDebitTransaction(false);
				}else{
					gLDTO.setAmount(objList.get(i).getAmountDebit());
					gLDTO.setDebitTransaction(true);
				}
			
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedAt()!=null)
					gLDTO.setCreatedAt(utility.getDateFromTimeStamp(objList.get(i).getCreatedAt().toString()));
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedBy()!=null)
					gLDTO.setCreatedBy(new ApplicationUserResponseDTO(objList.get(i).getCreatedBy().getUserId(), objList.get(i).getCreatedBy().getDisplayName()));
			
			gLDTO.setRequested_Action(true);
				
			gLDTOList.add(gLDTO);
		}
		
		
		return gLDTOList;
	}
	
	
	
	public List<GeneralLedgerResponseDTO> fromSimSimWalletFeeToDtoConverter(List<GlForSimSimWalletAccount> objList) throws Exception{
		List<GeneralLedgerResponseDTO> gLDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		for(int i=0; i< objList.size();i++){
			GeneralLedgerResponseDTO gLDTO=new GeneralLedgerResponseDTO();
			
			gLDTO.setIdGl(objList.get(i).getIdGl());
			gLDTO.setGlDescription(objList.get(i).getGlDescription());
			gLDTO.setDisbursedLoanDTO(getDisbursedLoanDTOFromDisbursedLoan(objList.get(i).getDisbursedLoans()));
			gLDTO.setTxnId(objList.get(i).getTxnId());
			
				if(objList.get(i).getAmountCredit()!=null){
					gLDTO.setAmount(objList.get(i).getAmountCredit());
					gLDTO.setDebitTransaction(false);
				}else{
					gLDTO.setAmount(objList.get(i).getAmountDebit());
					gLDTO.setDebitTransaction(true);
				}
			
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedAt()!=null)
					gLDTO.setCreatedAt(utility.getDateFromTimeStamp(objList.get(i).getCreatedAt().toString()));
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedBy()!=null)
					gLDTO.setCreatedBy(new ApplicationUserResponseDTO(objList.get(i).getCreatedBy().getUserId(), objList.get(i).getCreatedBy().getDisplayName()));
					
				gLDTO.setRequested_Action(true);
			gLDTOList.add(gLDTO);
		}
		
		
		return gLDTOList;
	}
	
	
	
	public List<GeneralLedgerResponseDTO> fromPayableAccountToDtoConverter(List<GlForPayableAccount> objList) throws Exception{
		List<GeneralLedgerResponseDTO> gLDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		for(int i=0; i< objList.size();i++){
			GeneralLedgerResponseDTO gLDTO=new GeneralLedgerResponseDTO();
			
			gLDTO.setIdGl(objList.get(i).getIdGl());
			gLDTO.setGlDescription(objList.get(i).getGlDescription());
			gLDTO.setDisbursedLoanDTO(getDisbursedLoanDTOFromDisbursedLoan(objList.get(i).getDisbursedLoans()));
			gLDTO.setTxnId(objList.get(i).getTxnId());
			
				if(objList.get(i).getAmountCredit()!=null){
					gLDTO.setAmount(objList.get(i).getAmountCredit());
					gLDTO.setDebitTransaction(false);
				}else{
					gLDTO.setAmount(objList.get(i).getAmountDebit());
					gLDTO.setDebitTransaction(true);
				}
				
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedAt()!=null)
					gLDTO.setCreatedAt(utility.getDateFromTimeStamp(objList.get(i).getCreatedAt().toString()));
				
				if(objList!=null && objList.size()>0 && objList.get(i).getCreatedBy()!=null)
					gLDTO.setCreatedBy(new ApplicationUserResponseDTO(objList.get(i).getCreatedBy().getUserId(), objList.get(i).getCreatedBy().getDisplayName()));
					
				gLDTO.setRequested_Action(true);
			gLDTOList.add(gLDTO);
		}
		
		
		return gLDTOList;
	}
	
	
public DisbursedLoanResponseDTO getDisbursedLoanDTOFromDisbursedLoan(DisbursedLoans disbursedLoan){
		
		DisbursedLoanResponseDTO disbursedLoanDTO=new DisbursedLoanResponseDTO();
		disbursedLoanDTO.setId(disbursedLoan.getId());
		disbursedLoanDTO.setApplication(getLoanApplicationDTOFromLoanApplication(disbursedLoan.getApplication()));
		disbursedLoanDTO.setTransactionId(disbursedLoan.getTransactionId());
		disbursedLoanDTO.setLoanStatus(disbursedLoan.getLoanStatus());
		if(disbursedLoan.getDueDate()!=null)
		disbursedLoanDTO.setDueDate(utility.getDateFromTimeStamp(disbursedLoan.getDueDate().toString()));
		if(disbursedLoan.getGraceDueDate()!=null)
		disbursedLoanDTO.setGraceDueDate(utility.getDateFromTimeStamp(disbursedLoan.getGraceDueDate().toString()));
		
		return disbursedLoanDTO;
	}
	
	
	public LoanApplicationsResponseDTO getLoanApplicationDTOFromLoanApplication(LoanApplication loanApplication){
		
		LoanApplicationsResponseDTO loanApplicationDTO=new LoanApplicationsResponseDTO();
		loanApplicationDTO.setId(loanApplication.getId());
		loanApplicationDTO.setProduct(new ProductResponseDTO(loanApplication.getProduct().getId(), loanApplication.getProduct().getProductName(), loanApplication.getProduct().getProductCatagory()));
		loanApplicationDTO.setCustomer(new CustomerResponseDTO(loanApplication.getCustomer().getId(), loanApplication.getCustomer().getCellNoInString(), loanApplication.getCustomer().getFullName(), loanApplication.getCustomer().getPakistaniCNIC()));
		loanApplicationDTO.setWalletTypeId(loanApplication.getId());
		loanApplicationDTO.setRequestedamount(loanApplication.getRequestedamount());
		loanApplicationDTO.setStatus(loanApplication.getStatus().name());
		
		
	
		return loanApplicationDTO;
	}

	
	
	public Long getLoanOutstandingBalance(long loanId) {
		try {
			
			List<GlForLoanReceivableAccount> loanReceivableAccountEntriesList=generalLedgerDao.getLoanReceivableEntriesAgainstLoanId(loanId);
			List<GlForPayableAccount> payableAccountEntriesList=generalLedgerDao.getPayableAccountEntriesAgainstLoanId(loanId);
			long totalLoanReceivableDebitSum = 0;
			long totalLoanReceivableCreditSum = 0;
			long outStandingLoanReceivableBalance;
			long outStandingPayableBalance;
			
			long outStandingBalance;
			
			for(int i=0; i<loanReceivableAccountEntriesList.size();i++){
				if(loanReceivableAccountEntriesList.get(i).getAmountDebit()!=null)
					totalLoanReceivableDebitSum=totalLoanReceivableDebitSum+loanReceivableAccountEntriesList.get(i).getAmountDebit();
				
				if(loanReceivableAccountEntriesList.get(i).getAmountCredit()!=null)
					totalLoanReceivableCreditSum=totalLoanReceivableCreditSum+loanReceivableAccountEntriesList.get(i).getAmountCredit();
			}
			outStandingLoanReceivableBalance=totalLoanReceivableDebitSum-totalLoanReceivableCreditSum;
			
			long totalPayableDebitSum = 0;
			long totalPayableCreditSum = 0;
			
			for(int i=0; i<payableAccountEntriesList.size();i++){
				if(payableAccountEntriesList.get(i).getAmountDebit()!=null)
					totalPayableDebitSum=totalPayableDebitSum+payableAccountEntriesList.get(i).getAmountDebit();
				
				if(payableAccountEntriesList.get(i).getAmountCredit()!=null)
					totalPayableCreditSum=totalPayableCreditSum+payableAccountEntriesList.get(i).getAmountCredit();
			}
			outStandingPayableBalance=totalPayableDebitSum-totalPayableCreditSum;
			
			outStandingBalance=outStandingLoanReceivableBalance+outStandingPayableBalance;
			
			return outStandingBalance;
		} catch (Exception e) {
			LOG.error("Error",e);
			return null;
		}
	}
	
	public Long getLoanDueAmount(long loanId) {
		try {
			
			List<GlForLoanReceivableAccount> loanReceivableAccountEntriesList=generalLedgerDao.getLoanReceivableEntriesAgainstLoanId(loanId);
			long totalDueAmount = 0;
			
			//All debit entries in loan receivable are due on customer
			for(int i=0; i<loanReceivableAccountEntriesList.size();i++){
				if(loanReceivableAccountEntriesList.get(i).getAmountDebit()!=null)
					totalDueAmount=totalDueAmount+loanReceivableAccountEntriesList.get(i).getAmountDebit();
			}
			return totalDueAmount;
		} catch (Exception e) {
			LOG.error("Error",e);
			return null;
		}
	}
	
	
	public Long getBalanceForSpecificGlAgainstLoanId(long loanId) {
		try {
			//This methods will need alteration. 
			List<GlForInternalLoanBook> internalLoanBookEntriesList=generalLedgerDao.getInternalBookEntriesAgainstLoanId(loanId);
			long totalInternalLoanBookDebitSum = 0;
			long totalInternalLoanBookCreditSum = 0;
			long outStandingInternalLoanBookBalance;
			
			for(int i=0; i<internalLoanBookEntriesList.size();i++){
				if(internalLoanBookEntriesList.get(i).getAmountDebit()!=null)
					totalInternalLoanBookDebitSum=totalInternalLoanBookDebitSum+internalLoanBookEntriesList.get(i).getAmountDebit();
				
				if(internalLoanBookEntriesList.get(i).getAmountCredit()!=null)
					totalInternalLoanBookCreditSum=totalInternalLoanBookCreditSum+internalLoanBookEntriesList.get(i).getAmountCredit();
			}
			outStandingInternalLoanBookBalance=totalInternalLoanBookCreditSum-totalInternalLoanBookDebitSum;
			
			return outStandingInternalLoanBookBalance;
		} catch (Exception e) {
			LOG.error("Error",e);
			return null;
		}
	}
	
	/*public List<GlForOneLoadWalletAccount> getLoanEntriesFromGlByLoanId(int glType,long loanId) {
		try {
			List<GlForOneLoadWalletAccount> internalLoanBookEntriesList = null;
			ListOfGeneralLedgerEntriesDTO listOfGLEntriesDTO=new ListOfGeneralLedgerEntriesDTO();
			
			if(glType==Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT){
				internalLoanBookEntriesList=generalLedgerDao.getOneLoadAccountEntriesAgainstLoanId(loanId);
				
				for(GlForOneLoadWalletAccount glForOneLoad:internalLoanBookEntriesList){
					GeneralLedgerDTO generalLedgerDTO=new GeneralLedgerDTO();
					generalLedgerDTO.set
					
				}
			
			
			
			}else if(glType==Constants.GENERAL_LEDGER_ENTRY_FOR_SIMSIM_ACCOUNT){
				internalLoanBookEntriesList=generalLedgerDao.getSimSimAccountEntriesAgainstLoanId(loanId);
			}else{
				if(glType==Constants.GENERAL_LEDGER_ENTRY_FOR_JAZZCASH_ACCOUNT){
					internalLoanBookEntriesList=generalLedgerDao.getJazzCashAccountEntriesAgainstLoanId(loanId);
				}else{
					
				}
			}
			
			return internalLoanBookEntriesList;
		} catch (Exception e) {
			LOG.error("Error",e);
			return null;
		}
	}*/
	
	public List<GeneralLedgerResponseDTO> getLoanEntriesFromGlByLoanId(int glType,long loanId) throws Exception {
		List<GeneralLedgerResponseDTO> generalLedgerDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		try {
			if(glType==Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT){
				generalLedgerDTOList=fromOneLoadWalletToDtoConverter(generalLedgerDao.getOneLoadAccountEntriesAgainstLoanId(loanId));
			}else if(glType==Constants.GENERAL_LEDGER_ENTRY_FOR_SIMSIM_ACCOUNT){
				generalLedgerDTOList=fromSimSimWalletFeeToDtoConverter(generalLedgerDao.getSimSimAccountEntriesAgainstLoanId(loanId));
			}else{
				if(glType==Constants.GENERAL_LEDGER_ENTRY_FOR_JAZZCASH_ACCOUNT){
					generalLedgerDTOList=fromJazzCashWalletFeeToDtoConverter(generalLedgerDao.getJazzCashAccountEntriesAgainstLoanId(loanId));
				}else{
					
				}
			} 
			
			return generalLedgerDTOList;

		} catch (Exception e) {
			LOG.error("Error",e);
			throw e;
		}
		
	}
	
	public List<GeneralLedgerResponseDTO> getLoanEntriesFromLoanReceivableGLByLoanId(long loanId) {
		List<GeneralLedgerResponseDTO> generalLedgerDTOList=new ArrayList<GeneralLedgerResponseDTO>();
		
		try {
				generalLedgerDTOList=fromLoanReceivableToDtoConverterForLoanSummary(generalLedgerDao.getLoanReceivableAccountEntriesAgainstLoanId(loanId));

		} catch (Exception e) {
			LOG.error("Error",e);
		}
		return generalLedgerDTOList;
	}
	
	
	public Boolean isLoanRepaymentIsOverDue(Date loanDisbursementDate, String loanPeriod, String loanGracePeriod){
		
		if(LOG.isInfoEnabled()){
			LOG.info("GeneralLedgerService.isLoanRepaymentIsOverDue()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Loan Disbursement Date: {} ",new Object[]{loanDisbursementDate});
			LOG.info("Loan Period(Duration): {} ",new Object[]{loanPeriod});
			LOG.info("Loan Grace Period: {} ",new Object[]{loanGracePeriod});
		}
		
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(loanDisbursementDate);
			 
			cal.add(Calendar.DAY_OF_MONTH, getSum(loanPeriod,loanGracePeriod)); 
			
			Date cutOffDate = cal.getTime();
			LOG.debug("Cust Off Date is: {} ",new Object[]{cutOffDate}); 
			
			Date currentDate=new Date();
			LOG.debug("Current Date is: {} ",new Object[]{new Date()}); 

			if(currentDate.before(cutOffDate)){
				LOG.debug("CutOff Date is Not Passed Yet. So,Good to Go.");
				return false;
			}else{
				LOG.debug("CutOff Date is Passed. So, Returning True to Add Late Payment Fee in GLs");
				return true;
			}
		}catch(Exception e){
			LOG.debug("Exception is: {}",new Object[]{e});
			return null;
		}finally{
			if(LOG.isDebugEnabled()){
				LOG.info("GeneralLedgerService.isLoanRepaymentIsOverDue()--End");
			}
		}
	}
	
	public int getSum(String loanPeriodDays, String loanGracePeriodDays){
		return Long.valueOf(loanPeriodDays).intValue()+Long.valueOf(loanGracePeriodDays).intValue();
	}
	
	public long getFedAmount(long amount, long fedPercentage){
		return (amount/100)*fedPercentage;
	}
	
	
	
	public Date getRequiredDate(Date baseDate, int numberOfDaysToAddInBaseDate){
		
		if(LOG.isInfoEnabled()){
			LOG.info("GeneralLedgerService.getRequiredDate()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Base Date(Date, In which Number of Days will be Added): {} ",new Object[]{baseDate});
			LOG.info("Number Of Days (To Add in Base Date): {} ",new Object[]{numberOfDaysToAddInBaseDate});
		}
		
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(baseDate);
			 
			cal.add(Calendar.DAY_OF_MONTH, numberOfDaysToAddInBaseDate); 
			
			Date requiredDate = cal.getTime();
			LOG.debug("Required Date(After Date Addition) is: {} ",new Object[]{requiredDate}); 
			
			return requiredDate;
		}catch(Exception e){
			LOG.debug("Exception is: {}",new Object[]{e});
			return null;
		}finally{
			if(LOG.isDebugEnabled()){
				LOG.info("GeneralLedgerService.getRequiredDate()--End");
			}
		}
	}
	
	
	/*public Boolean updateLoanStatusAccordingToOutstandingBalance(int loanId, DisbursedLoans disbursedLoan, ApplicationUsers loggedInUser){
		
		if(LOG.isInfoEnabled()){
			LOG.info("GeneralLedgerService.updateLoanStatusAccordingToOutstandingBalance()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Loan Id: {} ",new Object[]{loanId});
		}
		
		try{
//			getLoanOutstandingBalance(loanId);
			if(getLoanOutstandingBalance(loanId)>0){
				LOG.debug("No Need to Change Loan Status, As Outstanding Balance is Still Positive");
				return false;
			}else{
				disbursedLoan.setLoanStatus(disbursedLoan.getLoanStatus());
				
				if(objLMSDAO.updateDisbursedLoanStatusUsingLoanId(disbursedLoan.getId(), disbursedLoan, loggedInUser)){
					return true;
				}else{
					return false;
				}
			}
			
		}catch(Exception e){
			LOG.debug("Exception is: {}",new Object[]{e});
			return null;
		}finally{
			if(LOG.isDebugEnabled()){
				LOG.info("GeneralLedgerService.getRequiredDate()--End");
			}
		}
	}*/
	
	
	
public Long getDifferenceBetweenTwoDateInDays(Date baseDate, Date dueDate){
		
		if(LOG.isInfoEnabled()){
			LOG.info("GeneralLedgerService.differenceBetweenTwoDateInDays()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Base Date: {} ",new Object[]{baseDate});
			LOG.info("Due Date: {} ",new Object[]{baseDate});
		}
		
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

		try {
		    Date dueDateInDate = dateFormate.parse(String.valueOf(dueDate));
		    long diff = dueDateInDate.getTime() - new Date().getTime();
		    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
		    e.printStackTrace();
		    return null;
		}finally{
			if(LOG.isDebugEnabled()){
				LOG.info("GeneralLedgerService.differenceBetweenTwoDateInDays()--End");
			}
		}
	}


public void disburseApprovedLoanUsingTxn(int applicationId, String authString) throws Exception{
	
	if(LOG.isInfoEnabled()){
		LOG.info("GeneralLedgerService.disburseApprovedLoanUsingTxn()--Start");
		LOG.info("Received Parameters are:");
		LOG.info("Application Id: {} ",new Object[]{applicationId});
		LOG.info("Auth String: {} ",new Object[]{authString});
	}
	
	try {
		generalLedgerDao.disburseApprovedLoanUsingTxnInDAO(applicationId,authString);
		
	} catch (Exception ex){
			throw ex;
	}finally{
		if(LOG.isDebugEnabled()){
			LOG.info("GeneralLedgerService.disburseApprovedLoanUsingTxn()--End");
		}
	}
}



public void repayLoanUsingTransaction(long cellNo, long repayLoanAmount, String authString) throws Exception{
	
	if(LOG.isInfoEnabled()){
		LOG.info("GeneralLedgerService.repayLoanUsingTransaction()--Start");
		LOG.info("Received Parameters are:");
		LOG.info("Cell No: {} ",new Object[]{cellNo});
		LOG.info("Loan Repay Amount: {} ",new Object[]{repayLoanAmount});
		LOG.info("authString Id: {} ",new Object[]{authString});
	}
	
	try {
		generalLedgerDao.repayLoanAmountUsingTransaction(cellNo, repayLoanAmount, authString);
		
	} catch (Exception ex){
			throw ex;
	}finally{
		if(LOG.isDebugEnabled()){
			LOG.info("GeneralLedgerService.repayLoanUsingTransaction()--End");
		}
	}
}


/*public String convertAmountToUnitString(long amount) throws Exception{
	
	if(LOG.isInfoEnabled()){
		LOG.info("GeneralLedgerService.convertAmountToString()--Start");
		LOG.info("Received Parameters are:");
		LOG.info("Amount: {} ",new Object[]{amount});
	}
	
	try {
		Preferences objPreference = objLMSDAO.getPreferenceValueUsingName("activeCurrency");
		if(objPreference!=null){
			return objPreference.getPreferenceValue()+""+amount;
		}else{
			throw new Exception("Preference Value with Prefernce Name: activeCurrency Not Found");
		}
	
	} catch (Exception ex){
			throw ex;
	}finally{
		if(LOG.isDebugEnabled()){
			LOG.info("GeneralLedgerService.repayLoanUsingTransaction()--End");
		}
	}
}*/
	
	
}
