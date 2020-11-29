package out;

import org.springframework.stereotype.Service;

@Service
public class MyService {
    public MyService() {
        System.out.println("Bean from outer component-scan : " + this.getClass());
    }
}
