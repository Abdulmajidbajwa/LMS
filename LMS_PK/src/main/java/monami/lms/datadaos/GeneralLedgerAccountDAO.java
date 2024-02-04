package monami.lms.datadaos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import monami.lms.dataentities.ApplicationUsers;
import monami.lms.dataentities.Customer;
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
import monami.lms.dataentities.Product;
import monami.lms.dataentities.ProductSpecification;
import monami.lms.responceentities.BasicResponce;
import monami.lms.responceentities.ResponceWithMessage;
import monami.lms.utilities.Constants;
import monami.lms.utilities.DisbursedLoanStatus;
import monami.lms.utilities.LoanApplicationStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository
@Scope("prototype")
public class GeneralLedgerAccountDAO {
	Logger LOG = LoggerFactory.getLogger(GeneralLedgerAccountDAO.class);
	
	@Autowired DAO obj;
	
	public int financialEntryForLoanReceivableAccountGL(int glId, String glDescription, int loanId, String transactionId, long transactionAmount, boolean isDebitTransaction, ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();
			
			GlForLoanReceivableAccount glBean = new GlForLoanReceivableAccount();

			glBean.setIdGl(glId);
			glBean.setGlDescription(glDescription);
			
			glBean.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", loanId));
			glBean.setTxnId(transactionId);
			
			if(isDebitTransaction){
				glBean.setAmountCredit(null);
				glBean.setAmountDebit(transactionAmount);
			}else{
				glBean.setAmountCredit(transactionAmount);
				glBean.setAmountDebit(null);
			}
			glBean.setCreatedBy(loggedInUser);
			
			return obj.saveObject(glBean);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	
	public int financialEntryForServiceFeeIncomeAccountGL(int glId, String glDescription, int loanId, String transactionId, long transactionAmount, boolean isDebitTransaction, ApplicationUsers loggedInUser) throws Exception {	
		synchronized (DAO.class){ try {
			obj.startTask();
			
			GlForServiceFeeIncomeAccount glBean = new GlForServiceFeeIncomeAccount();

			glBean.setIdGl(glId);
			glBean.setGlDescription(glDescription);
			
			glBean.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", loanId));
			glBean.setTxnId(transactionId);
			
			if(isDebitTransaction){
				glBean.setAmountCredit(null);
				glBean.setAmountDebit(transactionAmount);
			}else{
				glBean.setAmountCredit(transactionAmount);
				glBean.setAmountDebit(null);
			}
			glBean.setCreatedBy(loggedInUser);
			
			
			
			return obj.saveObject(glBean);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	public int financialEntryForLatePaymentFeeIncomeAccountGL(int glId, String glDescription, int loanId, String transactionId, long transactionAmount, boolean isDebitTransaction, ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();
			
			GlForLatePaymentFeeIncomeAccount glBean = new GlForLatePaymentFeeIncomeAccount();

			glBean.setIdGl(glId);
			glBean.setGlDescription(glDescription);
			
			glBean.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", loanId));
			glBean.setTxnId(transactionId);
			
			if(isDebitTransaction){
				glBean.setAmountCredit(null);
				glBean.setAmountDebit(transactionAmount);
			}else{
				glBean.setAmountCredit(transactionAmount);
				glBean.setAmountDebit(null);
			}
			glBean.setCreatedBy(loggedInUser);
			
			
			
			return obj.saveObject(glBean);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	public int financialEntryForFEDLiabilityAccountGL(int glId, String glDescription, int loanId, String transactionId, long transactionAmount, boolean isDebitTransaction, ApplicationUsers loggedInUser) throws Exception {
		synchronized (DAO.class){ try {
			obj.startTask();
			
			GlForFedLiabilityAccount glBean = new GlForFedLiabilityAccount();

			glBean.setIdGl(glId);
			glBean.setGlDescription(glDescription);
			
			glBean.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", loanId));
			glBean.setTxnId(transactionId);
			
			if(isDebitTransaction){
				glBean.setAmountCredit(null);
				glBean.setAmountDebit(transactionAmount);
			}else{
				glBean.setAmountCredit(transactionAmount);
				glBean.setAmountDebit(null);
			}
			glBean.setCreatedBy(loggedInUser);
			
			return obj.saveObject(glBean);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	public int financialEntryForOneLoadWalletAccountGL(int glId, String glDescription, int loanId, String transactionId, long transactionAmount, boolean isDebitTransaction, ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();
			
			GlForOneLoadWalletAccount glBean = new GlForOneLoadWalletAccount();

			glBean.setIdGl(glId);
			glBean.setGlDescription(glDescription);
			
			glBean.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", loanId));
			glBean.setTxnId(transactionId);
			
			if(isDebitTransaction){
				glBean.setAmountCredit(null);
				glBean.setAmountDebit(transactionAmount);
			}else{
				glBean.setAmountCredit(transactionAmount);
				glBean.setAmountDebit(null);
			}
			glBean.setCreatedBy(loggedInUser);
			
			return obj.saveObject(glBean);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	public int financialEntryForSimSimWalletAccountGL(int glId, String glDescription, int loanId, String transactionId, long transactionAmount, boolean isDebitTransaction, ApplicationUsers loggedInUser) throws Exception {	
		synchronized (DAO.class){ try {
			obj.startTask();
			
			GlForSimSimWalletAccount glBean = new GlForSimSimWalletAccount();

			glBean.setIdGl(glId);
			glBean.setGlDescription(glDescription);
			
			glBean.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", loanId));
			glBean.setTxnId(transactionId);
			
			if(isDebitTransaction){
				glBean.setAmountCredit(null);
				glBean.setAmountDebit(transactionAmount);
			}else{
				glBean.setAmountCredit(transactionAmount);
				glBean.setAmountDebit(null);
			}
			glBean.setCreatedBy(loggedInUser);
			
			return obj.saveObject(glBean);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	public int financialEntryForJazzCashWalletAccountGL(int glId, String glDescription, int loanId, String transactionId, long transactionAmount, boolean isDebitTransaction, ApplicationUsers loggedInUser) throws Exception {	
		synchronized (DAO.class){ try {
			obj.startTask();
			
			GlForJazzCashWalletAccount glBean = new GlForJazzCashWalletAccount();

			glBean.setIdGl(glId);
			glBean.setGlDescription(glDescription);
			
			glBean.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", loanId));
			glBean.setTxnId(transactionId);
			
			if(isDebitTransaction){
				glBean.setAmountCredit(null);
				glBean.setAmountDebit(transactionAmount);
			}else{
				glBean.setAmountCredit(transactionAmount);
				glBean.setAmountDebit(null);
			}
			glBean.setCreatedBy(loggedInUser);
			
			
			return obj.saveObject(glBean);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	public int financialEntryForPayableAccountGL(int glId, String glDescription, int loanId, String transactionId, long transactionAmount, boolean isDebitTransaction, ApplicationUsers loggedInUser) throws Exception {
		synchronized (DAO.class){ try {
			obj.startTask();
			
			GlForPayableAccount glBean = new GlForPayableAccount();

			glBean.setIdGl(glId);
			glBean.setGlDescription(glDescription);
			
			glBean.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", loanId));
			glBean.setTxnId(transactionId);
			
			if(isDebitTransaction){
				glBean.setAmountCredit(null);
				glBean.setAmountDebit(transactionAmount);
			}else{
				glBean.setAmountCredit(transactionAmount);
				glBean.setAmountDebit(null);
			}
			glBean.setCreatedBy(loggedInUser);
		
			
			return obj.saveObject(glBean);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	
	public int financialEntryForInternalLoanBookGL(int glId, String glDescription, int loanId, String transactionId, long transactionAmount, boolean isDebitTransaction, ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();
			
			GlForInternalLoanBook glBean = new GlForInternalLoanBook();

			glBean.setIdGl(glId);
			glBean.setGlDescription(glDescription);
			
			glBean.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", loanId));
			glBean.setTxnId(transactionId);
			
			if(isDebitTransaction){
				glBean.setAmountCredit(null);
				glBean.setAmountDebit(transactionAmount);
			}else{
				glBean.setAmountCredit(transactionAmount);
				glBean.setAmountDebit(null);
			}
			glBean.setCreatedBy(loggedInUser);
			
			return obj.saveObject(glBean);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<GlForLoanReceivableAccount> getLoanReceivableAccountGL () {
			
		synchronized (DAO.class) {
			try {
				obj.startTask();
				List<GlForLoanReceivableAccount> toRetrun;
				try {
					toRetrun = obj.getAll(GlForLoanReceivableAccount.class);
					if (toRetrun != null) {
						return toRetrun;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			} finally {
				try {
					obj.endTask();
				} catch (Exception e) {
					System.out.println("Exception Occurred : " + e);
				}
			}
		}
		}

	@SuppressWarnings("unchecked")
		public List<GlForServiceFeeIncomeAccount> getServiceFeeIncomeAccountGL () {
				
			synchronized (DAO.class) {
				try {
					obj.startTask();
					List<GlForServiceFeeIncomeAccount> toRetrun;
					try {
						toRetrun = obj.getAll(GlForServiceFeeIncomeAccount.class);
						if (toRetrun != null) {
							return toRetrun;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				} finally {
					try {
						obj.endTask();
					} catch (Exception e) {
						System.out.println("Exception Occurred : " + e);
					}
				}
			}
			}

	
	public List<GlForLatePaymentFeeIncomeAccount> getLatePaymentFeeIncomeAccountGL () {
				
			synchronized (DAO.class) {
				try {
					obj.startTask();
					List<GlForLatePaymentFeeIncomeAccount> toRetrun;
					try {
						toRetrun = obj.getAll(GlForLatePaymentFeeIncomeAccount.class);
						if (toRetrun != null) {
							return toRetrun;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				} finally {
					try {
						obj.endTask();
					} catch (Exception e) {
						System.out.println("Exception Occurred : " + e);
					}
				}
			}
			}
		
				public List<GlForFedLiabilityAccount> getFEDLiabilityAccountGL () {
						
					synchronized (DAO.class) {
						try {
							obj.startTask();
							List<GlForFedLiabilityAccount> toRetrun;
							try {
								toRetrun = obj.getAll(GlForFedLiabilityAccount.class);
								if (toRetrun != null) {
									return toRetrun;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						} finally {
							try {
								obj.endTask();
							} catch (Exception e) {
								System.out.println("Exception Occurred : " + e);
							}
						}
					}
					}
				
				public List<GlForOneLoadWalletAccount> getOneLoadWalletAccountGL () {
						
					synchronized (DAO.class) {
						try {
							obj.startTask();
							List<GlForOneLoadWalletAccount> toRetrun;
							try {
								toRetrun = obj.getAll(GlForOneLoadWalletAccount.class);
								if (toRetrun != null) {
									return toRetrun;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						} finally {
							try {
								obj.endTask();
							} catch (Exception e) {
								System.out.println("Exception Occurred : " + e);
							}
						}
					}	
					}
				
				public List<GlForSimSimWalletAccount> getSimSimWalletAccountGL () {
						
					synchronized (DAO.class) {
						try {
							obj.startTask();
							List<GlForSimSimWalletAccount> toRetrun;
							try {
								toRetrun = obj.getAll(GlForSimSimWalletAccount.class);
								if (toRetrun != null) {
									return toRetrun;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						} finally {
							try {
								obj.endTask();
							} catch (Exception e) {
								System.out.println("Exception Occurred : " + e);
							}
						}
					}	
					}
				
				public List<GlForJazzCashWalletAccount> getJazzCashWalletAccountGL () {
						
					synchronized (DAO.class) {
						try {
							obj.startTask();
							List<GlForJazzCashWalletAccount> toRetrun;
							try {
								toRetrun = obj.getAll(GlForJazzCashWalletAccount.class);
								if (toRetrun != null) {
									return toRetrun;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						} finally {
							try {
								obj.endTask();
							} catch (Exception e) {
								System.out.println("Exception Occurred : " + e);
							}
						}
					}
					}
				
				public List<GlForPayableAccount> getPayableAccountGL () {
						
					synchronized (DAO.class) {
						try {
							obj.startTask();
							List<GlForPayableAccount> toRetrun;
							try {
								toRetrun = obj.getAll(GlForPayableAccount.class);
								if (toRetrun != null) {
									return toRetrun;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						} finally {
							try {
								obj.endTask();
							} catch (Exception e) {
								System.out.println("Exception Occurred : " + e);
							}
						}
					}	
					}
				
				public List<GlForInternalLoanBook> getInternalLoanBookGL () {
						
					synchronized (DAO.class) {
						try {
							obj.startTask();
							List<GlForInternalLoanBook> toRetrun;
							try {
								toRetrun = obj.getAll(GlForInternalLoanBook.class);
								if (toRetrun != null) {
									return toRetrun;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						} finally {
							try {
								obj.endTask();
							} catch (Exception e) {
								System.out.println("Exception Occurred : " + e);
							}
						}
					}		
					}
				
				
				
				@SuppressWarnings("unchecked")
				public List<GlForLoanReceivableAccount> getLoanReceivableEntriesAgainstLoanId (Long loanId) {
						
					synchronized (DAO.class) {
						try {
							obj.startTask();
							List<GlForLoanReceivableAccount> toRetrun;
							try {
								
								DisbursedLoans disbursedLoan= (DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class,"id",loanId.intValue());
								
								toRetrun =  obj.getAllOnCriteria(GlForLoanReceivableAccount.class,"disbursedLoans",disbursedLoan);
								if (toRetrun != null) {
									return toRetrun;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						} finally {
							try {
								obj.endTask();
							} catch (Exception e) {
								System.out.println("Exception Occurred : " + e);
							}
						}
					}
					}
				
				@SuppressWarnings("unchecked")
				public List<GlForPayableAccount> getPayableAccountEntriesAgainstLoanId (Long loanId) {
						
					synchronized (DAO.class) {
						try {
							obj.startTask();
							List<GlForPayableAccount> toRetrun;
							try {
								
								DisbursedLoans disbursedLoan= (DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class,"id",loanId.intValue());
								
								toRetrun =  obj.getAllOnCriteria(GlForPayableAccount.class,"disbursedLoans",disbursedLoan);
								if (toRetrun != null) {
									return toRetrun;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						} finally {
							try {
								obj.endTask();
							} catch (Exception e) {
								System.out.println("Exception Occurred : " + e);
							}
						}
					}
					}
				
				
				@SuppressWarnings("unchecked")
				public List<GlForInternalLoanBook> getInternalBookEntriesAgainstLoanId (Long loanId) {
						
					synchronized (DAO.class) {
						try {
							obj.startTask();
							List<GlForInternalLoanBook> toRetrun;
							try {
								
								DisbursedLoans disbursedLoan= (DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class,"id",loanId.intValue());
								
								toRetrun =  obj.getAllOnCriteria(GlForInternalLoanBook.class,"disbursedLoans",disbursedLoan);
								if (toRetrun != null) {
									return toRetrun;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						} finally {
							try {
								obj.endTask();
							} catch (Exception e) {
								System.out.println("Exception Occurred : " + e);
							}
						}
					}
					}
				
				
	@SuppressWarnings("unchecked")
	public List<GlForOneLoadWalletAccount> getOneLoadAccountEntriesAgainstLoanId(Long loanId) {

		synchronized (DAO.class) {
			try {
				obj.startTask();
				List<GlForOneLoadWalletAccount> result;
				List<GlForOneLoadWalletAccount> toReturn=new ArrayList<GlForOneLoadWalletAccount>();
				try {
					
					DisbursedLoans disbursedLoan= (DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class,"id",loanId.intValue());
					
					result = obj.getAllOnCriteria(GlForOneLoadWalletAccount.class, "disbursedLoans",disbursedLoan);
					
					for(GlForOneLoadWalletAccount glOneLoad:result){
						if(glOneLoad.getAmountDebit()!=null && glOneLoad.getAmountCredit()==null){
							toReturn.add(glOneLoad);
						}
					}
					
					if (toReturn != null) {
						return toReturn;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			} finally {
				try {
					obj.endTask();
				} catch (Exception e) {
					System.out.println("Exception Occurred : " + e);
				}
			}
		}
	}
				
	@SuppressWarnings("unchecked")
	public List<GlForSimSimWalletAccount> getSimSimAccountEntriesAgainstLoanId(Long loanId) {

		synchronized (DAO.class) {
			try {
				obj.startTask();
				List<GlForSimSimWalletAccount> toRetrun;
				try {
					
					
					DisbursedLoans disbursedLoan= (DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class,"id",loanId.intValue());
					
					toRetrun = obj.getAllOnCriteria(GlForSimSimWalletAccount.class, "disbursedLoans",disbursedLoan);
					
					if (toRetrun != null) {
						return toRetrun;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			} finally {
				try {
					obj.endTask();
				} catch (Exception e) {
					System.out.println("Exception Occurred : " + e);
				}
			}
		}
	}
				
	@SuppressWarnings("unchecked")
	public List<GlForJazzCashWalletAccount> getJazzCashAccountEntriesAgainstLoanId(Long loanId) {

		synchronized (DAO.class) {
			try {
				obj.startTask();
				List<GlForJazzCashWalletAccount> toRetrun;
				try {
					
					DisbursedLoans disbursedLoan= (DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class,"id",loanId.intValue());
						
					toRetrun = obj.getAllOnCriteria(GlForJazzCashWalletAccount.class, "disbursedLoans",disbursedLoan);
					
					
					if (toRetrun != null) {
						return toRetrun;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			} finally {
				try {
					obj.endTask();
				} catch (Exception e) {
					System.out.println("Exception Occurred : " + e);
				}
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<GlForLoanReceivableAccount> getLoanReceivableAccountEntriesAgainstLoanId(Long loanId) {

		synchronized (DAO.class) {
			try {
				obj.startTask();
				List<GlForLoanReceivableAccount> result;
				List<GlForLoanReceivableAccount> toRetrun= new ArrayList<GlForLoanReceivableAccount>();
				
				try {
					
					DisbursedLoans disbursedLoan= (DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class,"id",loanId.intValue());
						
					result = obj.getAllOnCriteria(GlForLoanReceivableAccount.class, "disbursedLoans",disbursedLoan);
					
					for(GlForLoanReceivableAccount glOneLoad:result){
						if(glOneLoad.getAmountDebit()!=null && glOneLoad.getAmountCredit()==null){
							toRetrun.add(glOneLoad);
						}
					}
					
					if (toRetrun != null) {
						return toRetrun;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			} finally {
				try {
					obj.endTask();
				} catch (Exception e) {
					System.out.println("Exception Occurred : " + e);
				}
			}
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public long getTotalRepaymentAmountAgainstSpecificOneLoadWallet(Long loanId) {

		synchronized (DAO.class) {
			try {
				obj.startTask();
				List<GlForOneLoadWalletAccount> result;
				try {
					
					DisbursedLoans disbursedLoan= (DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class,"id",loanId.intValue());
					result = obj.getAllOnCriteria(GlForOneLoadWalletAccount.class, "disbursedLoans",disbursedLoan);
					
					long totalRepaymentAmount = 0;
					
					for(GlForOneLoadWalletAccount glOneLoad:result){
						if(glOneLoad.getAmountDebit()!=null && glOneLoad.getAmountCredit()==null){
							totalRepaymentAmount=totalRepaymentAmount+glOneLoad.getAmountDebit().longValue();
						}
					}
					
					return totalRepaymentAmount;
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
			} finally {
				try {
					obj.endTask();
				} catch (Exception e) {
//					System.out.println("Exception Occurred : " + e);
				}
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public long getTotalPayableAmountFromLoanReceivableAgainstLoanId(Long loanId) {

		synchronized (DAO.class) {
			try {
				obj.startTask();
				try {
					obj.startTask();
					List<GlForLoanReceivableAccount> result;
					try {
						
						DisbursedLoans disbursedLoan= (DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class,"id",loanId.intValue());
						result = obj.getAllOnCriteria(GlForLoanReceivableAccount.class, "disbursedLoans",disbursedLoan);
						
						long totalPayableAmount = 0;
						
						for(GlForLoanReceivableAccount glLoanReceivable:result){
							if(glLoanReceivable.getAmountDebit()!=null && glLoanReceivable.getAmountCredit()==null){
								totalPayableAmount=totalPayableAmount+glLoanReceivable.getAmountDebit().longValue();
							}
						}
						
						return totalPayableAmount;
					} catch (Exception e) {
						e.printStackTrace();
						return -1;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
			} finally {
				try {
					obj.endTask();
				} catch (Exception e) {
//					System.out.println("Exception Occurred : " + e);
				}
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public long getOutstandingBalanceAgainstLoan(Long loanId) {

		synchronized (DAO.class) {
			try {
				obj.startTask();
				try {
					DisbursedLoans disbursedLoan= (DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class,"id",loanId.intValue());
					
					List<GlForLoanReceivableAccount> loanReceivableAccountEntriesList =  obj.getAllOnCriteria(GlForLoanReceivableAccount.class,"disbursedLoans",disbursedLoan);
					List<GlForPayableAccount> payableAccountEntriesList =  obj.getAllOnCriteria(GlForPayableAccount.class,"disbursedLoans",disbursedLoan);
					Long outStandingBalance=getOutStandingBalance(loanReceivableAccountEntriesList, payableAccountEntriesList);
					
					return outStandingBalance;
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
			} finally {
				try {
					obj.endTask();
				} catch (Exception e) {
//					System.out.println("Exception Occurred : " + e);
				}
			}
		}
	}
	
public void disburseApprovedLoanUsingTxnInDAO(int applicationId, String authString) throws Exception{
		
		if(LOG.isInfoEnabled()){
			LOG.info("LMSDAO.disburseApprovedLoanUsingTxnInDAO()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Application Id: {} ",new Object[]{applicationId});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}
		
		try {
			obj.startTask();	
			
			LoanApplication objLoanApp = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", applicationId);
			DisbursedLoans disbursedLoan = (DisbursedLoans) 	obj.getRowByAttribute(DisbursedLoans.class, "application", objLoanApp);
			
			
			if(disbursedLoan==null){
				LoanApplication loanApplication = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", applicationId);
				
				List<ProductSpecification> productAssumption = null;//=getProductSpecificationAgainstProduct(loanApplication.getProduct().getId());
				Product objProduct = (Product) 	obj.getRowByAttribute(Product.class, "id", loanApplication.getProduct().getId());				
				if(objProduct!=null){			
					HashSet<ProductSpecification> toParse=new HashSet<ProductSpecification>(objProduct.getProductSpecification());
					if(toParse!=null){
						productAssumption=new ArrayList<ProductSpecification>(toParse);
					}				
				}
				
				String loanPeriod = null;
				String gracePeriod = null;

				if(productAssumption==null){
					throw new Exception("Product Assumption Not Found, Unable to Process");
				}
				
				for(int i=0;i<productAssumption.size();i++){
					if(productAssumption.get(i).getProductSpecificationAssumption().getName().replace(" ", "").equalsIgnoreCase("LoanDuration")){
						LOG.info("Loan Period Days: {} ",new Object[]{productAssumption.get(i).getAssumptionValue()});
						loanPeriod=productAssumption.get(i).getAssumptionValue();
					}

					if(productAssumption.get(i).getProductSpecificationAssumption().getName().replace(" ", "").equalsIgnoreCase("gracePeriod")){
						LOG.info("Grace Period Days: {} ",new Object[]{productAssumption.get(i).getAssumptionValue()});
						gracePeriod=productAssumption.get(i).getAssumptionValue();
					}
				}
				
				if(loanPeriod!=null || gracePeriod!=null){
					LoanApplication objLoanApplication = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", applicationId);
					if(objLoanApplication!=null) {
						DisbursedLoans objDisbursedLoans=new DisbursedLoans();
						objDisbursedLoans.setApplication(objLoanApplication);
						objDisbursedLoans.setTransactionId(UUID.randomUUID().toString());
						objDisbursedLoans.setLoanStatus(DisbursedLoanStatus.ACTIVE);
						objDisbursedLoans.setDueDate(getRequiredDate(new Date(), Integer.valueOf(loanPeriod).intValue()));
						objDisbursedLoans.setGraceDueDate(getRequiredDate(new Date(), getSum(loanPeriod,gracePeriod)));
						objDisbursedLoans.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
						obj.saveObject(objDisbursedLoans);
					}else{
						throw new Exception("Loan Application Not Found");
					}	


					DisbursedLoans recentlyDisbursedLoan = (DisbursedLoans) 	obj.getRowByAttribute(DisbursedLoans.class, "application", objLoanApp);
					
					if(recentlyDisbursedLoan!=null) {

						GlForLoanReceivableAccount glBeanToSavePrincipleAmountInLoanReceivable = new GlForLoanReceivableAccount();
						glBeanToSavePrincipleAmountInLoanReceivable.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT);
						glBeanToSavePrincipleAmountInLoanReceivable.setGlDescription("Principal Disbursement");
						glBeanToSavePrincipleAmountInLoanReceivable.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", recentlyDisbursedLoan.getId()));
						glBeanToSavePrincipleAmountInLoanReceivable.setTxnId(recentlyDisbursedLoan.getTransactionId());
						glBeanToSavePrincipleAmountInLoanReceivable.setAmountCredit(null);
						glBeanToSavePrincipleAmountInLoanReceivable.setAmountDebit(Long.valueOf(recentlyDisbursedLoan.getApplication().getRequestedamount()));
						glBeanToSavePrincipleAmountInLoanReceivable.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
						int operationResultForOneLoad = obj.saveObject(glBeanToSavePrincipleAmountInLoanReceivable);
						
						if (operationResultForOneLoad > 0) {
							
							GlForInternalLoanBook glBeanToSavePrincipleAmountInInternalBook = new GlForInternalLoanBook();
							glBeanToSavePrincipleAmountInInternalBook.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_INTERNAL_LOAN_BOOK_ACCOUNT);
							glBeanToSavePrincipleAmountInInternalBook.setGlDescription("Principal Disbursement");
							glBeanToSavePrincipleAmountInInternalBook.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", recentlyDisbursedLoan.getId()));
							glBeanToSavePrincipleAmountInInternalBook.setTxnId(recentlyDisbursedLoan.getTransactionId());
							glBeanToSavePrincipleAmountInInternalBook.setAmountCredit(Long.valueOf(recentlyDisbursedLoan.getApplication().getRequestedamount()));
							glBeanToSavePrincipleAmountInInternalBook.setAmountDebit(null);
							glBeanToSavePrincipleAmountInInternalBook.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
							operationResultForOneLoad= obj.saveObject(glBeanToSavePrincipleAmountInInternalBook);
							
							if (operationResultForOneLoad > 0) {
								
								GlForOneLoadWalletAccount glToSavePrincipleAmountInOneLoadBook = new GlForOneLoadWalletAccount();
								glToSavePrincipleAmountInOneLoadBook.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT);
								glToSavePrincipleAmountInOneLoadBook.setGlDescription("Principal Disbursement");
								glToSavePrincipleAmountInOneLoadBook.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", recentlyDisbursedLoan.getId()));
								glToSavePrincipleAmountInOneLoadBook.setTxnId(recentlyDisbursedLoan.getTransactionId());
								glToSavePrincipleAmountInOneLoadBook.setAmountCredit(Long.valueOf(recentlyDisbursedLoan.getApplication().getRequestedamount()));
								glToSavePrincipleAmountInOneLoadBook.setAmountDebit(null);
								glToSavePrincipleAmountInOneLoadBook.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
								
								operationResultForOneLoad= obj.saveObject(glToSavePrincipleAmountInOneLoadBook);
								
								if (operationResultForOneLoad > 0) {
									LOG.info("One Load Entry(Loan Receivable, Internal Loan Book, One Load Wallet) Fully Recorded, Good to GO For Next Entries");
								} else {
									throw new Exception(
											"Failed to Record One Load Entry");
								}
							} else {
								throw new Exception(
										"Failed to Record Internal Book Entry");
							}
						} else {
							throw new Exception(
									"Failed to Record Loan Receivable Entry");
						}
						
						GlForLoanReceivableAccount glBeanToRecordServiceFeeInLoanReceivable = new GlForLoanReceivableAccount();
						glBeanToRecordServiceFeeInLoanReceivable.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT);
						glBeanToRecordServiceFeeInLoanReceivable.setGlDescription("Service Fee");
						glBeanToRecordServiceFeeInLoanReceivable.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", recentlyDisbursedLoan.getId()));
						glBeanToRecordServiceFeeInLoanReceivable.setTxnId(recentlyDisbursedLoan.getTransactionId());
						glBeanToRecordServiceFeeInLoanReceivable.setAmountCredit(null);
						glBeanToRecordServiceFeeInLoanReceivable.setAmountDebit(100L);
						glBeanToRecordServiceFeeInLoanReceivable.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
						int operationResultForServiceFee = obj.saveObject(glBeanToRecordServiceFeeInLoanReceivable);

						if (operationResultForServiceFee > 0) {
							
							GlForServiceFeeIncomeAccount glBeanToRecordServiceFeeInServiceFeeAccount = new GlForServiceFeeIncomeAccount();
							glBeanToRecordServiceFeeInServiceFeeAccount.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_SERVICE_FEE_ACCOUNT);
							glBeanToRecordServiceFeeInServiceFeeAccount.setGlDescription("Service Fee");
							glBeanToRecordServiceFeeInServiceFeeAccount.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", recentlyDisbursedLoan.getId()));
							glBeanToRecordServiceFeeInServiceFeeAccount.setTxnId(recentlyDisbursedLoan.getTransactionId());
							glBeanToRecordServiceFeeInServiceFeeAccount.setAmountCredit(100L);
							glBeanToRecordServiceFeeInServiceFeeAccount.setAmountDebit(null);
							glBeanToRecordServiceFeeInServiceFeeAccount.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
							operationResultForServiceFee = obj.saveObject(glBeanToRecordServiceFeeInServiceFeeAccount);
							
							if (operationResultForServiceFee > 0) {
									LOG.info("Service Fee Entry(Loan Receivable, Service Fee) Fully Recorded, Good to GO For Next Entries");
							} else {
								throw new Exception(
										"Failed to Record Service Fee Entry");
							}
						} else {
							throw new Exception(
									"Failed to Record One Loan Receivable Entry");
						}	
						
						
						GlForLoanReceivableAccount glBeanToRecordFEDInLoanReceivable = new GlForLoanReceivableAccount();
						glBeanToRecordFEDInLoanReceivable.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT);
						glBeanToRecordFEDInLoanReceivable.setGlDescription("SF FED Charge");
						glBeanToRecordFEDInLoanReceivable.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", recentlyDisbursedLoan.getId()));
						glBeanToRecordFEDInLoanReceivable.setTxnId(recentlyDisbursedLoan.getTransactionId());
						glBeanToRecordFEDInLoanReceivable.setAmountCredit(null);
						glBeanToRecordFEDInLoanReceivable.setAmountDebit(16L);
						glBeanToRecordFEDInLoanReceivable.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
						int operationResultForFED = obj.saveObject(glBeanToRecordFEDInLoanReceivable);

						if (operationResultForFED > 0) {
							
							GlForFedLiabilityAccount glBeanToRecordFEDInInternalLoanBook = new GlForFedLiabilityAccount();
							glBeanToRecordFEDInInternalLoanBook.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_FED_ACCOUNT);
							glBeanToRecordFEDInInternalLoanBook.setGlDescription("SF FED Charge");
							glBeanToRecordFEDInInternalLoanBook.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", recentlyDisbursedLoan.getId()));
							glBeanToRecordFEDInInternalLoanBook.setTxnId(recentlyDisbursedLoan.getTransactionId());
							glBeanToRecordFEDInInternalLoanBook.setAmountCredit(16L);
							glBeanToRecordFEDInInternalLoanBook.setAmountDebit(null);
							glBeanToRecordFEDInInternalLoanBook.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
							operationResultForFED = obj.saveObject(glBeanToRecordFEDInInternalLoanBook);
							
							if (operationResultForFED > 0) {
									LOG.info("FED Entry(Loan Receivable, FED) Fully Recorded, Good to GO For Next Entries");
							} else {
								throw new Exception(
										"Failed to Record FED Entry");
							}
						} else {
							throw new Exception("Failed to Record One Loan Receivable Entry");
						}
						
							if(objLoanApplication!=null) {
								objLoanApplication.setStatus(LoanApplicationStatus.DISBURSED);
								objLoanApplication.setUpdatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
							}
						
						if(obj.updateObject(objLoanApplication)){
							
							//No Need to return Anything inCase Successful 
							//throwing Exception to Test
//							throw new Exception("Throwing Exception to check Rollback");
							
						}else{
							throw new Exception("Failed to Change Loan Application Status to Disbursed");
						}
					} else{
						throw new Exception("No Disbursed Loan Found Against Application Id: "+applicationId);
					} 
				}else{
					throw new Exception("Product Assumption Not Found. Unable to Process");
				}
			} else{
				throw new Exception("Loan Already Disbursed");
			}
		} catch (Exception ex){
				obj.rollbackTransaction();
				throw ex;
		}finally{
			obj.endTask();
		}
	}	
	
@SuppressWarnings("unchecked")
public void repayLoanAmountUsingTransaction(long cellNo, long repayLoanAmount, String authString) throws Exception{
		
		try {
			obj.startTask();
				Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", cellNo);
				
				List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);
				List<DisbursedLoans> toReturnDisbursedLoan = new ArrayList<DisbursedLoans>();

				if(listOfDisbursedLoans!=null && listOfDisbursedLoans.size()>0){ //first we will check for disbursed pending loan in disbursedLoan(waiting for completion of repayment) 
					for(DisbursedLoans i:listOfDisbursedLoans){
						if(i.getApplication().getCustomer().getId()==objCustomer.getId() && i.getApplication().getStatus()==LoanApplicationStatus.DISBURSED){
							toReturnDisbursedLoan.add(i);
						}			
					}
				}else{
					throw new Exception("Not A Single Disbursed Loan Found");
				}
				if(toReturnDisbursedLoan!=null && toReturnDisbursedLoan.size()>0){
					DisbursedLoans disbursedLoan=null;
					if(toReturnDisbursedLoan.size()==1){
						disbursedLoan=toReturnDisbursedLoan.get(0);
					}else{
						throw new Exception("Inconsistent State. Multiple Disbursed Loans Found Against CellNo: "+cellNo);
					}
					
					if(disbursedLoan!=null) {

						List<GlForLoanReceivableAccount> loanReceivableAccountEntriesList =  obj.getAllOnCriteria(GlForLoanReceivableAccount.class,"disbursedLoans",disbursedLoan);
						List<GlForPayableAccount> payableAccountEntriesList =  obj.getAllOnCriteria(GlForPayableAccount.class,"disbursedLoans",disbursedLoan);
						Long outStandingBalance=getOutStandingBalance(loanReceivableAccountEntriesList, payableAccountEntriesList);

						if(repayLoanAmount>=outStandingBalance){
							if(outStandingBalance<=0){
								GlForPayableAccount glBeanToRecordRepaymentInPayableAccount = new GlForPayableAccount();
								glBeanToRecordRepaymentInPayableAccount.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_PAYABLE_ACCOUNT);
								glBeanToRecordRepaymentInPayableAccount.setGlDescription("Repayment");
								glBeanToRecordRepaymentInPayableAccount.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId()));
								glBeanToRecordRepaymentInPayableAccount.setTxnId(disbursedLoan.getTransactionId());
								glBeanToRecordRepaymentInPayableAccount.setAmountCredit(repayLoanAmount);
								glBeanToRecordRepaymentInPayableAccount.setAmountDebit(null);
								glBeanToRecordRepaymentInPayableAccount.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
								int responePayableAccount = obj.saveObject(glBeanToRecordRepaymentInPayableAccount);
								
								if(responePayableAccount>0){
									//Do Nothing(Repayment is Successful)
								}else{
									throw new Exception("Failed to Record Repayment");
								}

							}else{
								Long totalAmountForLoanReceivableAccountEntry=outStandingBalance;
								Long totalAmountForPayableAccountEntry=repayLoanAmount-outStandingBalance;

								GlForLoanReceivableAccount glBeanToSavePrincipleAmountInLoanReceivable = new GlForLoanReceivableAccount();
								glBeanToSavePrincipleAmountInLoanReceivable.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT);
								glBeanToSavePrincipleAmountInLoanReceivable.setGlDescription("Repayment");
								glBeanToSavePrincipleAmountInLoanReceivable.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId()));
								glBeanToSavePrincipleAmountInLoanReceivable.setTxnId(disbursedLoan.getTransactionId());
								glBeanToSavePrincipleAmountInLoanReceivable.setAmountCredit(totalAmountForLoanReceivableAccountEntry);
								glBeanToSavePrincipleAmountInLoanReceivable.setAmountDebit(null);
								glBeanToSavePrincipleAmountInLoanReceivable.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
								int responeLoanReceivableAccount = obj.saveObject(glBeanToSavePrincipleAmountInLoanReceivable);
								
								if(responeLoanReceivableAccount>0){
									GlForPayableAccount glBeanToRecordRepaymentInPayableAccount = new GlForPayableAccount();
									glBeanToRecordRepaymentInPayableAccount.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_PAYABLE_ACCOUNT);
									glBeanToRecordRepaymentInPayableAccount.setGlDescription("Repayment");
									glBeanToRecordRepaymentInPayableAccount.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId()));
									glBeanToRecordRepaymentInPayableAccount.setTxnId(disbursedLoan.getTransactionId());
									glBeanToRecordRepaymentInPayableAccount.setAmountCredit(totalAmountForPayableAccountEntry);
									glBeanToRecordRepaymentInPayableAccount.setAmountDebit(null);
									glBeanToRecordRepaymentInPayableAccount.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
									int responePayableAccount = obj.saveObject(glBeanToRecordRepaymentInPayableAccount);

									if(responePayableAccount>0){
										GlForOneLoadWalletAccount glToSavePrincipleAmountInOneLoadBook = new GlForOneLoadWalletAccount();
										glToSavePrincipleAmountInOneLoadBook.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT);
										glToSavePrincipleAmountInOneLoadBook.setGlDescription("Repayment");
										glToSavePrincipleAmountInOneLoadBook.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId()));
										glToSavePrincipleAmountInOneLoadBook.setTxnId(disbursedLoan.getTransactionId());
										glToSavePrincipleAmountInOneLoadBook.setAmountCredit(null);
										glToSavePrincipleAmountInOneLoadBook.setAmountDebit(repayLoanAmount);
										glToSavePrincipleAmountInOneLoadBook.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
										int responeOneLoadAccount= obj.saveObject(glToSavePrincipleAmountInOneLoadBook);
										
										if(responeOneLoadAccount>0){
											List<GlForInternalLoanBook> internalLoanBookEntriesList= obj.getAllOnCriteria(GlForInternalLoanBook.class,"disbursedLoans",disbursedLoan);
											long totalInternalLoanBookDebitSum = 0;
											long totalInternalLoanBookCreditSum = 0;
											for(int i=0; i<internalLoanBookEntriesList.size();i++){
												if(internalLoanBookEntriesList.get(i).getAmountDebit()!=null)
													totalInternalLoanBookDebitSum=totalInternalLoanBookDebitSum+internalLoanBookEntriesList.get(i).getAmountDebit();
												if(internalLoanBookEntriesList.get(i).getAmountCredit()!=null)
													totalInternalLoanBookCreditSum=totalInternalLoanBookCreditSum+internalLoanBookEntriesList.get(i).getAmountCredit();
											}
											Long calculateInternalLoanBookEntryAmount= totalInternalLoanBookCreditSum-totalInternalLoanBookDebitSum;
											
											if(calculateInternalLoanBookEntryAmount!=null && calculateInternalLoanBookEntryAmount>0){
												if(repayLoanAmount>calculateInternalLoanBookEntryAmount){
													GlForInternalLoanBook glBeanToSavePrincipleAmountInInternalBook = new GlForInternalLoanBook();
													glBeanToSavePrincipleAmountInInternalBook.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_INTERNAL_LOAN_BOOK_ACCOUNT);
													glBeanToSavePrincipleAmountInInternalBook.setGlDescription("Repayment");
													glBeanToSavePrincipleAmountInInternalBook.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId()));
													glBeanToSavePrincipleAmountInInternalBook.setTxnId(disbursedLoan.getTransactionId());
													glBeanToSavePrincipleAmountInInternalBook.setAmountCredit(null);
													glBeanToSavePrincipleAmountInInternalBook.setAmountDebit(calculateInternalLoanBookEntryAmount);
													glBeanToSavePrincipleAmountInInternalBook.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
													int responeInternalLoanAccount= obj.saveObject(glBeanToSavePrincipleAmountInInternalBook);
													
													if(responeInternalLoanAccount>0){
														loanReceivableAccountEntriesList= obj.getAllOnCriteria(GlForLoanReceivableAccount.class,"disbursedLoans",disbursedLoan);
														payableAccountEntriesList= obj.getAllOnCriteria(GlForPayableAccount.class,"disbursedLoans",disbursedLoan);
														outStandingBalance=getOutStandingBalance(loanReceivableAccountEntriesList, payableAccountEntriesList);
														
														if(outStandingBalance>0){
															throw new Exception("No Need to Change Loan Status, As Outstanding Balance is Still Positive");
														}else{
															DisbursedLoans objDisbursedLoan= (DisbursedLoans) 	obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId());
															
															objDisbursedLoan.setExistingStatusJustBeforeFullPayment(disbursedLoan.getLoanStatus());
															objDisbursedLoan.setLoanStatus(DisbursedLoanStatus.PAID);

															if(obj.updateObject(objDisbursedLoan)){
																LoanApplication objLoanApplication = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", disbursedLoan.getApplication().getId());
																	if(objLoanApplication!=null) {
																		objLoanApplication.setStatus(LoanApplicationStatus.COMPLETED);
																		objLoanApplication.setUpdatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
																	}

																	if(obj.updateObject(objLoanApplication)){
																		//Do Nothing // Success Case
																	}else{
																		throw new Exception("Repayment is Successful And Loan Status is Also changed But Application Status is not Changed");
																	}
															}else{
																//Do Nothing // Success Case
															}
														}
													}else{
														throw new Exception("Failed to Complete Repayment Process Successfully");
													}
												}else{
													GlForInternalLoanBook glBeanToSavePrincipleAmountInInternalBook = new GlForInternalLoanBook();
													glBeanToSavePrincipleAmountInInternalBook.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_INTERNAL_LOAN_BOOK_ACCOUNT);
													glBeanToSavePrincipleAmountInInternalBook.setGlDescription("Repayment");
													glBeanToSavePrincipleAmountInInternalBook.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId()));
													glBeanToSavePrincipleAmountInInternalBook.setTxnId(disbursedLoan.getTransactionId());
													glBeanToSavePrincipleAmountInInternalBook.setAmountCredit(null);
													glBeanToSavePrincipleAmountInInternalBook.setAmountDebit(repayLoanAmount);
													glBeanToSavePrincipleAmountInInternalBook.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
													int responeInternalLoanAccount= obj.saveObject(glBeanToSavePrincipleAmountInInternalBook);
													
													if(responeInternalLoanAccount>0){
														loanReceivableAccountEntriesList= obj.getAllOnCriteria(GlForLoanReceivableAccount.class,"disbursedLoans",disbursedLoan);
														payableAccountEntriesList= obj.getAllOnCriteria(GlForPayableAccount.class,"disbursedLoans",disbursedLoan);
														outStandingBalance=getOutStandingBalance(loanReceivableAccountEntriesList, payableAccountEntriesList);
														
														
														if(outStandingBalance>0){
															throw new Exception("No Need to Change Loan Status, As Outstanding Balance is Still Positive");
														}else{
															DisbursedLoans objDisbursedLoan= (DisbursedLoans) 	obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId());
															objDisbursedLoan.setExistingStatusJustBeforeFullPayment(disbursedLoan.getLoanStatus());
															objDisbursedLoan.setLoanStatus(DisbursedLoanStatus.PAID);
															
															if(obj.updateObject(objDisbursedLoan)){
																LoanApplication objLoanApplication = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", disbursedLoan.getApplication().getId());
																	if(objLoanApplication!=null) {
																		objLoanApplication.setStatus(LoanApplicationStatus.COMPLETED);
																		objLoanApplication.setUpdatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
																	}
																	if(obj.updateObject(objLoanApplication)){
																		//Do Nothing // Success Case
																	}else{
																		throw new Exception("Repayment is Successful And Loan Status is Also changed But Application Status is not Changed");
																	}
															}else{
																//Do Nothing // Success Case
															}
														}
													}else{
														throw new Exception("Failed to Record Internal Book Entries");
													}
												}
											}else{
												loanReceivableAccountEntriesList= obj.getAllOnCriteria(GlForLoanReceivableAccount.class,"disbursedLoans",disbursedLoan);
												payableAccountEntriesList= obj.getAllOnCriteria(GlForPayableAccount.class,"disbursedLoans",disbursedLoan);
												outStandingBalance=getOutStandingBalance(loanReceivableAccountEntriesList, payableAccountEntriesList);
												
												if(outStandingBalance>0){
													throw new Exception("No Need to Change Loan Status, As Outstanding Balance is Still Positive");
												}else{
													DisbursedLoans objDisbursedLoan= (DisbursedLoans) 	obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId());
													objDisbursedLoan.setExistingStatusJustBeforeFullPayment(disbursedLoan.getLoanStatus());
													objDisbursedLoan.setLoanStatus(DisbursedLoanStatus.PAID);
													
													if(obj.updateObject(objDisbursedLoan)){
														LoanApplication objLoanApplication = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", disbursedLoan.getApplication().getId());
															if(objLoanApplication!=null) {
																objLoanApplication.setStatus(LoanApplicationStatus.COMPLETED);
																objLoanApplication.setUpdatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
															}
															if(obj.updateObject(objLoanApplication)){
																//Do Nothing // Success Case
															}else{
																throw new Exception("Repayment is Successful And Loan Status is Also changed But Application Status is not Changed");
															}
													}else{
														//Do Nothing // Success Case
													}
												}
											}

										}else{
											throw new Exception("Failed to Process Repayment Successfully");
										}
									}else{
										throw new Exception("Failed to Complete Repayment Process Successfully");
									}
								}else{
									throw new Exception("Failed to Initiate Repayment Successfully");
								}
							}
						}else{
							GlForLoanReceivableAccount glBeanToSavePrincipleAmountInLoanReceivable = new GlForLoanReceivableAccount();
							glBeanToSavePrincipleAmountInLoanReceivable.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT);
							glBeanToSavePrincipleAmountInLoanReceivable.setGlDescription("Repayment");
							glBeanToSavePrincipleAmountInLoanReceivable.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId()));
							glBeanToSavePrincipleAmountInLoanReceivable.setTxnId(disbursedLoan.getTransactionId());
							glBeanToSavePrincipleAmountInLoanReceivable.setAmountCredit(repayLoanAmount);
							glBeanToSavePrincipleAmountInLoanReceivable.setAmountDebit(null);
							glBeanToSavePrincipleAmountInLoanReceivable.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
							int responeLoanReceivableAccount = obj.saveObject(glBeanToSavePrincipleAmountInLoanReceivable);

							if(responeLoanReceivableAccount>0){
								GlForOneLoadWalletAccount glToSavePrincipleAmountInOneLoadBook = new GlForOneLoadWalletAccount();
								glToSavePrincipleAmountInOneLoadBook.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT);
								glToSavePrincipleAmountInOneLoadBook.setGlDescription("Repayment");
								glToSavePrincipleAmountInOneLoadBook.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId()));
								glToSavePrincipleAmountInOneLoadBook.setTxnId(disbursedLoan.getTransactionId());
								glToSavePrincipleAmountInOneLoadBook.setAmountCredit(null);
								glToSavePrincipleAmountInOneLoadBook.setAmountDebit(repayLoanAmount);
								glToSavePrincipleAmountInOneLoadBook.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
								int responeOneLoadAccount= obj.saveObject(glToSavePrincipleAmountInOneLoadBook);

								if(responeOneLoadAccount>0){

									List<GlForInternalLoanBook> internalLoanBookEntriesList=obj.getAllOnCriteria(GlForInternalLoanBook.class,"disbursedLoans",disbursedLoan);
									long totalInternalLoanBookDebitSum = 0;
									long totalInternalLoanBookCreditSum = 0;
									
									for(int i=0; i<internalLoanBookEntriesList.size();i++){
										if(internalLoanBookEntriesList.get(i).getAmountDebit()!=null)
											totalInternalLoanBookDebitSum=totalInternalLoanBookDebitSum+internalLoanBookEntriesList.get(i).getAmountDebit();
										if(internalLoanBookEntriesList.get(i).getAmountCredit()!=null)
											totalInternalLoanBookCreditSum=totalInternalLoanBookCreditSum+internalLoanBookEntriesList.get(i).getAmountCredit();
									}
									Long calculateInterLoanBookEntryAmount=totalInternalLoanBookCreditSum-totalInternalLoanBookDebitSum;
									
									if(calculateInterLoanBookEntryAmount!=null && calculateInterLoanBookEntryAmount>0){
										if(repayLoanAmount>calculateInterLoanBookEntryAmount){
											GlForInternalLoanBook glBeanToSavePrincipleAmountInInternalBook = new GlForInternalLoanBook();
											glBeanToSavePrincipleAmountInInternalBook.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_INTERNAL_LOAN_BOOK_ACCOUNT);
											glBeanToSavePrincipleAmountInInternalBook.setGlDescription("Repayment");
											glBeanToSavePrincipleAmountInInternalBook.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId()));
											glBeanToSavePrincipleAmountInInternalBook.setTxnId(disbursedLoan.getTransactionId());
											glBeanToSavePrincipleAmountInInternalBook.setAmountCredit(null);
											glBeanToSavePrincipleAmountInInternalBook.setAmountDebit(calculateInterLoanBookEntryAmount);
											glBeanToSavePrincipleAmountInInternalBook.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
											int responeInternalLoanAccount= obj.saveObject(glBeanToSavePrincipleAmountInInternalBook);
											
											if(responeInternalLoanAccount>0){
												//Do nothing.Repayment is Successfull
											}else{
												throw new Exception("Failed to Record GL(Internal Loan) Entry/Entries, Unable to Process");
												
											}
										}else{
											GlForInternalLoanBook glBeanToSavePrincipleAmountInInternalBook = new GlForInternalLoanBook();
											glBeanToSavePrincipleAmountInInternalBook.setIdGl(Constants.GENERAL_LEDGER_ENTRY_FOR_INTERNAL_LOAN_BOOK_ACCOUNT);
											glBeanToSavePrincipleAmountInInternalBook.setGlDescription("Repayment");
											glBeanToSavePrincipleAmountInInternalBook.setDisbursedLoans((DisbursedLoans) obj.getRowByAttribute(DisbursedLoans.class, "id", disbursedLoan.getId()));
											glBeanToSavePrincipleAmountInInternalBook.setTxnId(disbursedLoan.getTransactionId());
											glBeanToSavePrincipleAmountInInternalBook.setAmountCredit(null);
											glBeanToSavePrincipleAmountInInternalBook.setAmountDebit(repayLoanAmount);
											glBeanToSavePrincipleAmountInInternalBook.setCreatedBy((ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", authString));
											int responeInternalLoanAccount= obj.saveObject(glBeanToSavePrincipleAmountInInternalBook);
											
											if(responeInternalLoanAccount>0){
												//Do nothing.Repayment is Successfull
											}else{
												throw new Exception("Failed to Record GL(Internal Loan) Entry/Entries, Unable to Process");
											}
										}
									}else{
										//No Need to Record Internal Loan Book Entry as Principal Loan Amount Already Paid
//										--Success Case
									}

								}else{
									throw new Exception("Failed to Record GL(OneLoad) Entry/Entries, Unable to Process");
								}

							}else{
								throw new Exception("Failed to Record GL(Loan Receivable) Entry/Entries, Unable to Process");
							}
						}

					}else{
						throw new Exception("No Active Disbursed Loan Found Against Given Customer");
					}
				}else{
					throw new Exception("No Active Disbursed Loan Found Against Given Customer");
				}
			
		} catch (Exception ex){
				obj.rollbackTransaction();
				throw ex;
		}finally{
			obj.endTask();
		}
	}


private long getOutStandingBalance(List<GlForLoanReceivableAccount> loanReceivableAccountEntriesList, List<GlForPayableAccount> payableAccountEntriesList){
	long totalLoanReceivableDebitSum = 0;
	long totalLoanReceivableCreditSum = 0;
	long outStandingLoanReceivableBalance=0;
	long outStandingPayableBalance=0;
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

public int getSum(String loanPeriodDays, String loanGracePeriodDays){
	return Long.valueOf(loanPeriodDays).intValue()+Long.valueOf(loanGracePeriodDays).intValue();
}
	
}
