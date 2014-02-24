package com.adhub.services;

import java.io.IOException;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.util.EntityUtils;

import com.adhub.objects.Cliente;
import com.adhub.utils.HttpAction;
import com.adhub.utils.RestUtils;
import com.google.gson.Gson;

public class ClienteService extends HttpAction {
	private static final String GET_CLIENTE = RestUtils.SERVERURL + "/rest/cliente/busca/por/id/";
	private static final String UPDATE = RestUtils.SERVERURL + "/rest/macro/update";
	private HttpEntity entity;
	private Gson gson;

	public Cliente getCliente(int major) throws ClientProtocolException, IOException {
		entity = GET(GET_CLIENTE + major);
		gson = new Gson();
		Cliente cliente = null;
		if (entity != null) {
			cliente = gson.fromJson(EntityUtils.toString(entity, "UTF-8"), Cliente.class);
		}
		return cliente;
	}

	public void update(Cliente cliente) throws ClientProtocolException, IOException {
		entity = POST(UPDATE, cliente);
	}
}
