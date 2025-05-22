package com.example.softekhealth.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.softekhealth.domain.model.MoodType
import com.example.softekhealth.ui.theme.Angry
import com.example.softekhealth.ui.theme.Calm
import com.example.softekhealth.ui.theme.Happy
import com.example.softekhealth.ui.theme.Info
import com.example.softekhealth.ui.theme.Neutral
import com.example.softekhealth.ui.theme.Sad
import com.example.softekhealth.ui.theme.Stressed
import com.example.softekhealth.ui.theme.Success
import com.example.softekhealth.ui.theme.Warning
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToForms: () -> Unit,
    viewModel: HomeViewModel
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mind Compass") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { /* TODO: Implement logout */ }
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            // Seção de seleção de humor diário
            MoodSelectionSection(
                selectedMood = state.todayMood,
                onMoodSelected = { mood ->
                    viewModel.onEvent(HomeEvent.SelectMood(mood))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Métricas de bem-estar
            WellbeingMetricsSection(viewModel)

            Spacer(modifier = Modifier.height(24.dp))

            // Gráfico de tendências de humor
            MoodTrendChart(moods = state.recentMoods)

            Spacer(modifier = Modifier.height(24.dp))

            // Botão para autoavaliação completa
            Button(
                onClick = onNavigateToForms,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Realizar autoavaliação psicossocial")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Seção de dicas de bem-estar
            WellnessTipsSection()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MoodSelectionSection(
    selectedMood: MoodType?,
    onMoodSelected: (MoodType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Como você está se sentindo hoje?",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        val today = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("pt", "BR")).format(Date())
        Text(
            text = today.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val moods = listOf(
            MoodType.HAPPY to Happy,
            MoodType.ENERGETIC to Happy,
            MoodType.CALM to Calm,
            MoodType.NEUTRAL to Neutral,
            MoodType.SAD to Sad,
            MoodType.TIRED to Sad,
            MoodType.STRESSED to Stressed,
            MoodType.ANXIOUS to Stressed,
            MoodType.ANGRY to Angry
        )

        val moodEmojis = mapOf(
            MoodType.HAPPY to "😃",
            MoodType.ENERGETIC to "⚡",
            MoodType.CALM to "😌",
            MoodType.NEUTRAL to "😐",
            MoodType.SAD to "😔",
            MoodType.TIRED to "😴",
            MoodType.STRESSED to "😫",
            MoodType.ANXIOUS to "😰",
            MoodType.ANGRY to "😠"
        )

        val moodDescriptions = mapOf(
            MoodType.HAPPY to "Feliz",
            MoodType.ENERGETIC to "Energético",
            MoodType.CALM to "Calmo",
            MoodType.NEUTRAL to "Neutro",
            MoodType.SAD to "Triste",
            MoodType.TIRED to "Cansado",
            MoodType.STRESSED to "Estressado",
            MoodType.ANXIOUS to "Ansioso",
            MoodType.ANGRY to "Irritado"
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(moods) { (moodType, color) ->
                MoodItem(
                    emoji = moodEmojis[moodType] ?: "",
                    description = moodDescriptions[moodType] ?: "",
                    color = color,
                    isSelected = selectedMood == moodType,
                    onClick = { onMoodSelected(moodType) }
                )
            }
        }
    }
}

@Composable
fun MoodItem(
    emoji: String,
    description: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(if (isSelected) color else color.copy(alpha = 0.2f))
        ) {
            Text(
                text = emoji,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WellbeingMetricsSection(viewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Seu bem-estar em números",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                title = "Nível de Estresse",
                value = "${viewModel.calculateStressLevel()}/10",
                icon = Icons.Default.Speed,
                color = when (viewModel.calculateStressLevel()) {
                    in 0..3 -> Success
                    in 4..7 -> Warning
                    else -> Error
                },
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                title = "Humor Geral",
                value = "${viewModel.calculateMoodScore()}%",
                icon = Icons.Default.SentimentSatisfied,
                color = when (viewModel.calculateMoodScore()) {
                    in 0..30 -> Error
                    in 31..70 -> Warning
                    else -> Success
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                title = "Consistência",
                value = "${viewModel.calculateConsistencyScore()}%",
                icon = Icons.Default.DateRange,
                color = when (viewModel.calculateConsistencyScore()) {
                    in 0..30 -> Error
                    in 31..70 -> Warning
                    else -> Success
                },
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                title = "Autoavaliações",
                value = "1/5", // Placeholder para dados reais
                icon = Icons.Default.FormatListBulleted,
                color = Info,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
    }
}

@Composable
fun MoodTrendChart(moods: List<com.example.softekhealth.domain.model.Mood>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Tendência de humor - Últimas 2 semanas",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Gerar dados para o gráfico
        val calendar = Calendar.getInstance()
        val today = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, -13) // Voltar 13 dias
        
        // Criar um mapa de datas para os últimos 14 dias
        val dateMap = mutableMapOf<String, Float>()
        for (i in 0 until 14) {
            val dateKey = SimpleDateFormat("dd/MM", Locale.getDefault()).format(calendar.time)
            dateMap[dateKey] = 0f
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        
        // Preencher o mapa com os dados de humor disponíveis
        moods.forEach { mood ->
            val dateKey = SimpleDateFormat("dd/MM", Locale.getDefault()).format(mood.date)
            if (dateKey in dateMap) {
                dateMap[dateKey] = when (mood.moodType) {
                    MoodType.HAPPY, MoodType.ENERGETIC -> 5f
                    MoodType.CALM -> 4f
                    MoodType.NEUTRAL -> 3f
                    MoodType.TIRED, MoodType.SAD -> 2f
                    MoodType.STRESSED, MoodType.ANXIOUS, MoodType.ANGRY -> 1f
                }
            }
        }
        
        // Criar pontos para o gráfico
        val points = dateMap.entries.sortedBy { it.key }.mapIndexed { index, entry ->
            Point(index.toFloat(), entry.value)
        }

        // Implementação temporária simplificada enquanto resolvemos problemas com a biblioteca YCharts
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Gráfico",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Visualização de tendências",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${points.size} registros nos últimos 14 dias",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        if (points.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Registre seu humor diariamente para ver as tendências",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun WellnessTipsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Dicas de bem-estar",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
        ) {
            items(wellnessTips) { tip ->
                WellnessTipCard(tip)
            }
        }
    }
}

@Composable
fun WellnessTipCard(tip: WellnessTip) {
    Card(
        modifier = Modifier
            .size(width = 180.dp, height = 160.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { /* TODO: Implement tip detail view */ },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )
            
            Column {
                Text(
                    text = tip.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = tip.shortDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                    maxLines = 3
                )
            }
        }
    }
}

data class WellnessTip(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val fullContent: String
)

val wellnessTips = listOf(
    WellnessTip(
        id = 1,
        title = "Respiração consciente",
        shortDescription = "5 minutos de respiração profunda podem reduzir significativamente o estresse.",
        fullContent = "Técnica de respiração consciente: Inspire lentamente pelo nariz contando até 4, segure por 2, expire pela boca contando até 6. Repita por 5 minutos."
    ),
    WellnessTip(
        id = 2,
        title = "Pausas ativas",
        shortDescription = "Levante-se e faça uma pausa a cada 60 minutos de trabalho.",
        fullContent = "Dicas para pausas ativas: Alongue-se, caminhe por 5 minutos, faça exercícios de rotação do pescoço e ombros, olhe para um ponto distante para relaxar os olhos."
    ),
    WellnessTip(
        id = 3,
        title = "Hidratação",
        shortDescription = "Mantenha-se hidratado para melhorar a concentração e energia.",
        fullContent = "A desidratação, mesmo leve, pode afetar sua concentração e energia. Mantenha uma garrafa de água por perto e estabeleça metas de consumo diário. Experimente adicionar frutas para um sabor natural."
    ),
    WellnessTip(
        id = 4,
        title = "Mindfulness",
        shortDescription = "Pratique atenção plena para reduzir a ansiedade no trabalho.",
        fullContent = "Exercício rápido de mindfulness: Observe 5 coisas que você pode ver, 4 que pode tocar, 3 que pode ouvir, 2 que pode cheirar e 1 que pode provar. Este exercício ajuda a ancorar você no momento presente."
    ),
    WellnessTip(
        id = 5,
        title = "Conexão social",
        shortDescription = "Cultive relacionamentos positivos com os colegas de trabalho.",
        fullContent = "Dicas para conexões sociais no trabalho: Reserve tempo para almoçar com colegas, participe de eventos da empresa, crie um grupo de interesses comuns, ofereça ajuda quando possível e pratique a escuta ativa."
    )
)
