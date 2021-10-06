<script>
	import download from "downloadjs";

	let ownerName = "Василий Пупкин";
	let accountNumber = 47;
	let dateFrom = new Date().toISOString().slice(0, 10);
	let dateTo = new Date().toISOString().slice(0, 10);
	let rowAmount = 0;
	let allCapital = 0;
	let files = [];
	let templateFile = null;

	let rows = [];

	async function doPost() {
		const res = await fetch("api/template-document", {
			method: "POST",
			headers: {
				Accept: "application/octet-stream",
				"Content-Type": "application/json",
			},
			body: JSON.stringify({
				ownerName: ownerName,
				accountNumber: accountNumber,
				dateFrom: dateFrom,
				dateTo: dateTo,
				rows: rows,
				allCapital: allCapital,
				templateFile: templateFile,
			}),
		});

		const docFileData = await res.blob();
		download(docFileData, "document.docx", "application/octet-stream");
	}

	function onChange() {
		const file = files.files[0];

		const reader = new FileReader();
		reader.addEventListener(
			"load",
			function () {
				templateFile = reader.result.split(',')[1];
			},
			false
		);
		reader.readAsDataURL(file);
	}

	function getRandomInt(max) {
		return Math.floor(Math.random() * max);
	}

	function getRandomCurrency() {
		return ["USD", "BYN", "RUB", "EUR"][getRandomInt(4)];
	}

	function generateRow() {
		return {
			date: randomDate(),
			id: getRandomInt(1000000),
			description: "description" + getRandomInt(100),
			currency: getRandomCurrency(),
			amount: getRandomInt(3000000),
		};
	}

	function randomDate() {
		return new Date(+new Date() - Math.floor(Math.random() * 10000000000))
			.toISOString()
			.slice(0, 10);
	}

	function fillRows() {
		rows = [];
		for (let i = 0; i < rowAmount; ++i) {
			rows = [...rows, generateRow()];
		}
	}

	function downloadFiles() {
		doPost();
	}
</script>

<main role="main" class="container">
	<h2>Выписка по счету</h2>

	<div class="mb-3">
		<label for="ownerName" class="form-label">Владелец счета:</label>
		<input id="ownerName" bind:value={ownerName} />
	</div>
	<div class="mb-3">
		<label for="accountNumber" class="form-label">Номер счета:</label>
		<input id="accountNumber" type="number" bind:value={accountNumber} />
	</div>
	<div class="mb-3">
		<p>
			Период выписки: c 
			<input type="date" id="dateFrom" bind:value={dateFrom}/>
			по <input type="date" id="dateTo" bind:value={dateTo} />
		</p>
	</div>

	<div class="mb-3">
		<label for="allCapital" class="form-label"
			>Заменить все строчные буквы на заглавные:
		</label>
		<input id="allCapital" type="checkbox" bind:checked={allCapital} />
	</div>

	<p>
		<label for="templateFile">Выбрать шаблон</label>
		<input
			bind:this={files}
			on:change={onChange}
			type="file"
			id="templateFile"
			accept=".docx"
		/>
		<a href="template.docx">Пример шаблона</a>
	</p>

	<p>
		<label for="rowAmount">Количество записей:</label>
		<input
			id="rowAmount"
			on:change={fillRows}
			type="number"
			bind:value={rowAmount}
		/>
		<button class="btn btn-primary" on:click|preventDefault={downloadFiles}>
			Создать документ
		</button>
	</p>

	<div class="table-responsive">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th scope="col" class="dateCol">Дата</th>
					<th scope="col" class="idCol">Номер транзакции</th>
					<th scope="col" class="descriptionCol">Описание</th>
					<th scope="col" class="currencyCol">Валюта</th>
					<th scope="col" class="amountCol">Сумма</th>
				</tr>
			</thead>
			<tbody>
				{#each rows as { date, id, description, currency, amount }, i}
					<tr>
						<td class="dateCol">{date}</td>
						<td class="idCol">{id}</td>
						<td class="descriptionCol">{description}</td>
						<td class="currencyCol">{currency}</td>
						<td class="amountCol">{amount}</td>
					</tr>
				{/each}
			</tbody>
		</table>
	</div>

	<p>
		<a
			class="btn btn-primary"
			data-bs-toggle="collapse"
			href="#collapseExample"
			role="button"
			aria-expanded="false"
			aria-controls="collapseExample"
		>
			Автор
		</a>
	</p>
	<div class="collapse" id="collapseExample">
		<div class="card card-body">
			<dl>
				<dt>Студент</dt>
				<dd>Пищулёнок Максим Сергеевич</dd>

				<dt>Курс</dt>
				<dd>4</dd>

				<dt>Группа</dt>
				<dd>4</dd>

				<dt>Год</dt>
				<dd>2021</dd>
			</dl>
		</div>
	</div>
</main>

<style>
	.dateCol {
		text-align: center;
		text-decoration: underline;
	}

	.idCol {
		text-align: right;
		font-style: italic;
	}

	.descriptionCol {
		text-align: left;
	}

	.currencyCol {
		text-align: left;
		color: blue;
		font-style: oblique;
		font-style: italic;
	}

	.amountCol {
		text-align: right;
		color: red;
		font-style: oblique;
	}

	/* @import 'https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css'; */
</style>
