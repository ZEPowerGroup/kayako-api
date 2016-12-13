package org.penguin.kayako.adapters;

import java.util.Date;

public class NullableDateAdapter extends UnixDateAdapter {
    
    @Override
    public Integer marshal(Date v) throws Exception {
        return null == v ? 0 : super.marshal(v);
    }
    
    @Override
    public Date unmarshal(Integer v) throws Exception {
        return 0 == v ? null : super.unmarshal(v);
    }
    
}
