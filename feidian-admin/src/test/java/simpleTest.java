import com.feidian.sidebarTree.service.IGenotypeFileService;
import com.feidian.sidebarTree.utils.CsvUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.feidian.YuZhongApplication.class)
public class simpleTest {

    @Autowired
    private IGenotypeFileService genotypeFileService;


    @Test
    public void testExport() throws IOException {

    }

}
