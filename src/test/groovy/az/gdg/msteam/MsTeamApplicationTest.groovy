package az.gdg.msteam

import spock.lang.Specification

class MsTeamApplicationTest extends Specification {
    void setup() {
    }

    void cleanup() {
    }

    def "two plus two should equal four"() {
        given:
            int left = 2
            int right = 2

        when:
            int result = left + right

        then:
            result == 4
    };

    def "Should be able to remove from list"() {
        given:
            def list = [1, 2, 3, 4]

        when:
            list.remove(0)

        then:
            list == [2, 3, 4]
    }

    def "getting square rightness test"(int a, int b, int c) {
        expect:
            Math.pow(a, b) == c

        where:
            a | b | c
            1 | 2 | 1
            2 | 3 | 8
            3 | 2 | 9
    }
}
