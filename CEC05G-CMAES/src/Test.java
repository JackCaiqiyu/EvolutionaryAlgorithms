
public class Test {
	public static void main(String[] args) {
		for(int F=1; F<=25; F++) {
			for(int DIM=10; DIM<=50; DIM+=20) {
				Records records = new Records();
				Configuration.DIM = DIM;
				Configuration.F = F;
				Configuration.seed = seeds.getSeed(F);
				Configuration.records = new Records();
				int pop_size = (int)(10 + Math.floor(3*Math.log(DIM)));
				int MaxFunEvals = 10000*DIM;
				double fmin = 99999999;
				for(int run=0; run<25; run++) {
					while ((MaxFunEvals > pop_size) && (fmin > Bounds.Ter_Err)) {
						Cmaes cmaes = new Cmaes(pop_size);
						fmin = cmaes.execute();
						pop_size = pop_size * 2;
					}
				}
				records.write(DIM, F, "G-CMAES", false);
			}
		}
	} // main  
} // class
