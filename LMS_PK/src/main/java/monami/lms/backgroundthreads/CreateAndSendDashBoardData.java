package monami.lms.backgroundthreads;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import monami.lms.datadaos.LMSDAO;
import monami.lms.dataentities.DisbursedLoans;
import monami.lms.socketmessages.NewDashboardUpdate;
import monami.lms.webclientrestcontollers.WebClientRestContoller;
import monami.lms.websocket.WebsocketService;



@Component
public class CreateAndSendDashBoardData  implements DisposableBean, Runnable {
	Logger LOG = LoggerFactory.getLogger(CreateAndSendDashBoardData.class);

	
	

	@Autowired
	private WebsocketService objWebsocketService;
	private Thread thread;
	private volatile boolean someCondition;
	
	@Autowired
	private WebClientRestContoller controller;
	@Autowired 
	private LMSDAO objLMSDAO;

	CreateAndSendDashBoardData(){
		this.thread = new Thread(this);
		this.thread.start();
	}

	@Override
	public void run(){
		someCondition=true;
		while(someCondition){
			try {
				Thread.sleep(86400000);
				
				LOG.info("Calling Job to Mark Late Payment Fee");

				List<DisbursedLoans> dispLoanList;
				try {
					dispLoanList = objLMSDAO.getAllPaidOrUnPaidDisbursedLoans(false);
				
					for(DisbursedLoans dispLoan:dispLoanList){
						controller.checkAndMarkOverdueEntriesAgainstCustomerUsingCellNo(dispLoan.getApplication().getCustomer().getCellNo());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void destroy(){
		someCondition = false;
	}

	public NewDashboardUpdate createDashboardData() throws Exception{
		return null;
	}
}