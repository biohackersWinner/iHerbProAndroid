package ru.biohackers.iherb.android.data

import ru.biohackers.iherb.android.model.Bad
import ru.biohackers.iherb.android.model.BadCategory
import ru.biohackers.iherb.android.model.BadGroup
import ru.biohackers.iherb.android.model.Prescription

object Data {

    val CATEGORIES = listOf(
        BadCategory(id = 1, title = "Пост COVID"),
        BadCategory(id = 2, title = "Поддержание молодости"),
        BadCategory(id = 3, title = "Детоксикация"),
        BadCategory(id = 4, title = "Для пищеварения"),
        BadCategory(id = 4, title = "Сезонное обострение"),
    )

    val PRESCRIPTIONS = mutableListOf<Prescription>()

    val BAD_GROUPS = mutableListOf<BadGroup>().apply {
        add(
            BadGroup(
                id = 1,
                title = "Цитиколин",
                synonyms = listOf("цитиколин"),
                bads = listOf(),
            )
        )
        add(
            BadGroup(
                id = 2,
                title = "Сахаромицеты",
                synonyms = listOf("сахаромицет"),
                bads = listOf(),
            )
        )
        add(
            BadGroup(
                id = 3,
                title = "Одуванчик",
                synonyms = listOf("одуванчик"),
                bads = listOf(),
            )
        )
        add(
            BadGroup(
                id = 4,
                title = "Артишок",
                synonyms = listOf("артишок"),
                bads = listOf(),
            )
        )
        add(
            BadGroup(
                id = 5,
                title = "Сорбент",
                synonyms = listOf("сорбен"),
                bads = listOf(),
            )
        )
        add(
            BadGroup(
                id = 6,
                title = "Витамин D",
                synonyms = listOf("витамин d"),
                bads = listOf(),
            )
        )
        add(
            BadGroup(
                id = 7,
                title = "Витамин C",
                synonyms = listOf("витамин c"),
                bads = listOf(),
            )
        )
    }

    val BADS = listOf(
        Bad(
            id = 1,
            title = "Solaray, витамин C длительного высвобождения, 1000 мг, 100 таблеток",
            description = "Витамин C обладает антиоксидантными свойствами и предназначен для поддержания нормального синтеза коллагена, целостности капилляров и кровеносных сосудов, развития хрящей и костей, иммунитета и передачи нервных импульсов. Это средство с двухэтапным медленным высвобождением разработано так, чтобы первая половина содержащегося в продукте витамина С высвобождалась быстро, а вторая половина — постепенно в течение 12 часов**. Это может увеличить доступность витамина С для организма в течение более длительного периода времени.",
            categories = listOf(),
            imageUrl = "https://s3.images-iherb.com/sor/sor04453/l/3.jpg",
            characteristics = """
Срок годности: 01.05.24 0:00:00
Доступно, начиная с: 26.09.16 15:39:00
Вес в упаковке: 0.21 кг
Код товара: SOR-04453
UPC Код: 076280044539
Количество в упаковке: 100 штук
Размеры: 7.6 x 7.6 x 14 cm, 0.18 кг
            """.trimIndent(),
            synonyms = listOf("Витамин C", "Витамин С"),
        )
    )

}