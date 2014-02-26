package com.adhub.dao;

import java.io.IOException;
import java.util.Date;

import android.content.Context;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;

import com.adhub.objects.Produto;
import com.adhub.objects.Propaganda;
import com.adhub.services.ProdutoService;
import com.adhub.utils.Cache;

public abstract class ProdutoDAO {
	public static Produto getProduto(int major, int minor, Context context) throws ClientProtocolException, IOException {
		ProdutoService produtoService = new ProdutoService();
		Produto produto = null;

		produto = produtoService.getProduto(major, minor);
		produto.setLastUpdate(new Date().getTime());

		String key = Cache.makeKey(major, minor);

		Produto produtoCached = (Produto) Cache.readObject(context, key);
		if (produtoCached != null) {
			for (Propaganda propaganda : produtoCached.getPropagandas()) {
				if (propaganda != null && propaganda.isVisualized()) {
					Propaganda propagandaServer = produto.getPropaganda(propaganda.getProximidade());
					propagandaServer.setVisualized(propaganda.getUltimaAtualizacao() >= propagandaServer.getUltimaAtualizacao());
				}
			}
		}

		Cache.writeObject(context, key, produto);
		return produto;

	}
}
