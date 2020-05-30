import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


public class Test {
    public static void main(String...args) throws UnirestException {
        HttpResponse<String> response = Unirest.get("http://translate.google.cn/translate_a/single?client=gtx&dt=t&dj=1&ie=UTF-8&sl=auto&tl=zh-CN&q=applo")
                .asString();
        //response=Unirest.get("http://dict.youdao.com/w/a/").asString();
        System.out.println(response.getBody());
    }
}
