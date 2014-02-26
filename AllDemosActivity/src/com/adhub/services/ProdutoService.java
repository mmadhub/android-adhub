package com.adhub.services;

import java.io.IOException;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.util.EntityUtils;

import com.adhub.objects.Produto;
import com.adhub.utils.HttpAction;
import com.adhub.utils.RestUtils;
import com.google.gson.Gson;

public class ProdutoService extends HttpAction {
	private static final String GET_PRODUTO = RestUtils.SERVERURL + "/rest/produto/busca/por/majorid/";
	private HttpEntity entity;
	private Gson gson;

	public Produto getProduto(int major, int minor) throws ClientProtocolException, IOException {
		entity = GET(GET_PRODUTO + major + "/minorid/" + minor);
		gson = new Gson();
		Produto produto = null;
		if (entity != null) {
			produto = gson.fromJson(EntityUtils.toString(entity, "UTF-8"), Produto.class);
		}
		return produto;
	}
}
