private import SI::s;
private import ScalarValues::Boolean;

part def Led {
    attribute is_on: Boolean;
}

state LedStateMachine {
    in led : Led;

    entry; then Off;

    state Off {
        entry assign led.is_on := false;
    }
    accept after 0.5 [s] then On;

    state On {
        entry assign led.is_on := true;
    }
    accept after 1.5 [s] then Off;
}
