import com.mathworks.engine.*;
import com.mathworks.engine.MatlabEngine;

class javaFevalFunc{
    public static void main(String[] args) throws Exception{
        MatlabEngine eng = MatlabEngine.startMatlab();
        eng.eval("addpath('H:\\MatlabForJava\\matlab_java\\src\\test\\java')");
        double[] a = {2.0 ,4.0, 6.0};
        String method = "add";
        Integer p1 = 1;
        Integer p2 = 2;
        Integer results = eng.feval(1,method, p1,p2);

        System.out.println(results);
        eng.close();

    }
}