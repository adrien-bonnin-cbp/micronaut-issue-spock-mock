package example.micronaut

import javax.inject.Singleton

@Singleton
interface MathService {

    Integer compute(Integer num);
}
