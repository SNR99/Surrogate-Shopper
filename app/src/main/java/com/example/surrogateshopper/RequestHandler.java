package com.example.surrogateshopper;

import org.json.JSONException;

public interface RequestHandler {
  void processResponse(String response) throws JSONException;
}
