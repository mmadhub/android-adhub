package com.adhub.dao;

import java.io.IOException;

import android.content.Context;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;

import com.adhub.objects.Cliente;
import com.adhub.services.ClienteService;
import com.adhub.utils.Cache;

public abstract class ClienteDAO {

	public static Cliente getCliente(Context context, int major) {
		ClienteService clienteService = new ClienteService();
		Cliente cliente = null;

		String key = "Client_" + major;

		cliente = (Cliente) Cache.readObject(context, key);

		if (cliente != null) {
			return cliente;
		}

		try {
			cliente = clienteService.getCliente(major);
			Cache.writeObject(context, key, cliente);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cliente;
	}

	public static void update(Cliente cliente) throws ClientProtocolException, IOException {
		ClienteService clienteService = new ClienteService();
		try {
			clienteService.update(cliente);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
