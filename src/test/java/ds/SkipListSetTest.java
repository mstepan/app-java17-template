package ds;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

final class SkipListSetTest {


    @Test
    void add(){
        SkipListSet set = new SkipListSet();
        assertThat(set.toString()).isEqualTo("[]");

        set.add(10);
        assertThat(set.toString()).isEqualTo("[10]");

        set.add(20);
        assertThat(set.toString()).isEqualTo("[10, 20]");

        set.add(7);
        assertThat(set.toString()).isEqualTo("[7, 10, 20]");

        set.add(3);
        assertThat(set.toString()).isEqualTo("[3, 7, 10, 20]");

        set.add(17);
        assertThat(set.toString()).isEqualTo("[3, 7, 10, 17, 20]");

        set.add(31);
        assertThat(set.toString()).isEqualTo("[3, 7, 10, 17, 20, 31]");
    }

}
