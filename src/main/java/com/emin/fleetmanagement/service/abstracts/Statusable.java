package com.emin.fleetmanagement.service.abstracts;

import com.emin.fleetmanagement.utils.result.DataResult;

public interface Statusable {


    DataResult setStatus(String itemBarcode, int statusValue) throws Exception;










}
