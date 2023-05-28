import com.mathworks.engine.*;
import com.mathworks.engine.MatlabEngine;

class javaFevalFunc{
    public static void main(String[] args) throws Exception{
        MatlabEngine eng = MatlabEngine.startMatlab();
        eng.eval("addpath('H:\\MatlabForJava\\matlab_java\\src\\test\\java')");
        double[] a = {2.0 ,4.0, 6.0};
        Integer results = eng.feval(1,"add", 1,2);

        System.out.println(results);
        eng.close();

    }
}