import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class TestDemo {

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        System.out.println(MILLISECONDS.ordinal());
        long revTs = Long.MAX_VALUE - System.currentTimeMillis();

        System.out.println(revTs);

        System.out.println(Long.MAX_VALUE / (100 * 10 * 10000 * 10000));

        MD5Hash.getMD5AsHex("asdfjowfsdkjfowejfosdjfowjfiwjefiojsdiojfowiejf");
    }

}
