import org.junit.Test;
import static org.junit.Assert.assertEquals;
/**
 * Created by framg on 06/05/2016.
 */
public class TestBenchmark {

    @Test
    public void Benchmark15LFun5(){
        CEC15Benchmark benchmarkLearning = new CEC15Benchmark(10, 5);
        double []x = {44.064898688431610, -57.965198017032080, -71.398834348380120, 46.018242218020870, 81.862397730987340, 82.203847931393650, 76.751101636120180, 67.399556442702450, 81.479228448199450, 85.516188519508690};
        System.out.println(benchmarkLearning.f(x));
        //assertEquals("4.818109477097459e03");
    }
}
