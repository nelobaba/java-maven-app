import com.example.Application;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test()
    public void testApp(){
        Application application = new Application();
        String result = application.getStatus();
        assertEquals("OK", result);
    }
}