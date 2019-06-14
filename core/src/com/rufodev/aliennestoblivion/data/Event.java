package com.rufodev.aliennestoblivion.data;

public class Event {
    int type;
    double timer;
    int code;

    public Event (int type,double timer, int code){
        this.type = type;
        this.timer = timer;
        this.code = code;
    }

    public void update (double timeElapsed){
        if (timeElapsed >= timer + System.currentTimeMillis()){
            //TODO PERFORM EVENT
            switch(type){
                case 1 :
                    if (code == 111){
                        //Spawn some unit;
                    }
                    if (code == 222){
                        //spawn a different unit;
                    }
                    break;
                case 2 :
                    break;
                default:
                    break;
            }
        }
    }
}
