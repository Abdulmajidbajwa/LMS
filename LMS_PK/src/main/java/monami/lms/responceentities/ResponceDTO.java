package monami.lms.responceentities;

public class ResponceDTO<T> extends BasicResponce{
	    private T t;

	 
		public ResponceDTO(boolean r,T t) {
			super(r);
			this.t = t;
		}
		public T getT() {
			return t;
		}
		public void setT(T t) {
			this.t = t;
		}
	    
	    
}
