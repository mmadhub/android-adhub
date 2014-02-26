package com.adhub.dao;

import java.io.IOException;

import ch.boye.httpclientandroidlib.client.ClientProtocolException;

import com.adhub.objects.Cliente;
import com.adhub.services.ClienteService;

public abstract class ClienteDAO {

	public static Cliente getCliente(int major) {
		ClienteService clienteService = new ClienteService();
		Cliente cliente = null;
		try {
			cliente = clienteService.getCliente(major);
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
