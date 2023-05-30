package ds;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

final class SkipListSetTest {


    @Test
    void addAndContains(){
        SkipListSet set = new SkipListSet();

        set.add(10);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isFalse();
        assertThat(set.contains(7)).isFalse();
        assertThat(set.contains(3)).isFalse();
        assertThat(set.contains(17)).isFalse();
        assertThat(set.contains(31)).isFalse();

        set.add(20);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isTrue();
        assertThat(set.contains(7)).isFalse();
        assertThat(set.contains(3)).isFalse();
        assertThat(set.contains(17)).isFalse();
        assertThat(set.contains(31)).isFalse();

        set.add(7);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isTrue();
        assertThat(set.contains(7)).isTrue();
        assertThat(set.contains(3)).isFalse();
        assertThat(set.contains(17)).isFalse();
        assertThat(set.contains(31)).isFalse();

        set.add(3);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isTrue();
        assertThat(set.contains(7)).isTrue();
        assertThat(set.contains(3)).isTrue();
        assertThat(set.contains(17)).isFalse();
        assertThat(set.contains(31)).isFalse();

        set.add(17);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isTrue();
        assertThat(set.contains(7)).isTrue();
        assertThat(set.contains(3)).isTrue();
        assertThat(set.contains(17)).isTrue();
        assertThat(set.contains(31)).isFalse();

        set.add(31);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isTrue();
        assertThat(set.contains(7)).isTrue();
        assertThat(set.contains(3)).isTrue();
        assertThat(set.contains(17)).isTrue();
        assertThat(set.contains(31)).isTrue();

        assertThat(set.contains(-10)).isFalse();
        assertThat(set.contains(0)).isFalse();
        assertThat(set.contains(1)).isFalse();
        assertThat(set.contains(11)).isFalse();
        assertThat(set.contains(18)).isFalse();
        assertThat(set.contains(32)).isFalse();
        assertThat(set.contains(133)).isFalse();
    }

    @Test
    void checkToString(){
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

    @Test
    void checkToStringReverse(){
        SkipListSet set = new SkipListSet();
        assertThat(set.toStringReverse()).isEqualTo("[]");

        set.add(10);
        assertThat(set.toStringReverse()).isEqualTo("[10]");

        set.add(20);
        assertThat(set.toStringReverse()).isEqualTo("[20, 10]");

        set.add(7);
        assertThat(set.toStringReverse()).isEqualTo("[20, 10, 7]");

        set.add(3);
        assertThat(set.toStringReverse()).isEqualTo("[20, 10, 7, 3]");

        set.add(17);
        assertThat(set.toStringReverse()).isEqualTo("[20, 17, 10, 7, 3]");

        set.add(31);
        assertThat(set.toStringReverse()).isEqualTo("[31, 20, 17, 10, 7, 3]");
    }

}
