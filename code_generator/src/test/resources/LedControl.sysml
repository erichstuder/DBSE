package LedControl {
    private import SI::s;

    enum def LedState {
        On;
        Off;
    }

    enum def SystemStates {
        Initializing;
        Running;
        Error;
    }

//     // action set_led { in value : LedState; }

//     // state LedStateMachine {
//     //     entry;
//     //     then Off;

//     //     state Off {
//     //         entry set_led { in value = LedState::Off; }
//     //     }
//     //     accept after 0.5 [s] then On;

//     //     state On {
//     //         entry set_led { in value = LedState::On; }
//     //     }
//     //     accept after 1.5 [s] then Off;
//     // }
}
