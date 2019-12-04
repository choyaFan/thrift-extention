import com.choy.thriftplus.core.filter.FilterProcessor;
import com.choy.thriftplus.core.filter.FilterScanner;
import com.choy.thriftplus.eureka.ThriftEurekaClient;
import com.choy.thriftplus.eureka.test.gen.ExternalServiceS;
import com.choy.thriftplus.eureka.test.gen.ObjectIdGenerator;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;

public class FilterService implements ExternalServiceS.Iface {
    @Override
    public String externalService(String token) throws TException {
        if(doPreFilter(token)){
        ThriftEurekaClient session = new ThriftEurekaClient();
        TProtocol protocol = session.getConnection("eureka.vipAddress");
        ObjectIdGenerator.Client client = new ObjectIdGenerator.Client(protocol);
        session.returnConnection(protocol);
        return client.getObjectId("123");
        }
        return "token error";
    }

    public boolean doPreFilter(String token){
        try {
            FilterScanner scanner = new FilterScanner();
            scanner.ofPackage("com.choy.thriftplus.core.filter.filters")
                    .initFilter();
            FilterProcessor.Instance.preRouting(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
