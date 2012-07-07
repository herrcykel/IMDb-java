package imdb.parser;



public abstract class Video extends Title {
	protected int runtime;
	
	public Video(String id) {
		super(id);
	}

	public int getRuntime() {
		return runtime;
	}

	public String getRuntimeStr() {
		int h, m, s;
		h = this.runtime / (60 * 60);
		m = (this.runtime % (60 * 60)) / 60;
		s = this.runtime - (m * 60 + h * 60 * 60);

		if (h > 0) {
			return String.format("%02d:%02d:%02d", h, m, s);
		} 
		else {
			return String.format("%02d:%02d", m, s);
		}
	}

	/*@Override
	public void testPrint() {
		final int TA_BORT;
		super.testPrint();
		System.out.println("Runtime: " + this.getRuntimeStr());
	}*/
	
	

}
