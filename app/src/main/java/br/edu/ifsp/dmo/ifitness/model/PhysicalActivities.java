package br.edu.ifsp.dmo.ifitness.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "physical_activities")
public class PhysicalActivities {

    @NonNull
    @PrimaryKey
    private String id;
    private String user;
    private String activityCategory;
    private String distance;
    private String duration;
    private String activityDate;

}

/*2. A aplicação deve persistir os dados de todas as atividades físicas. Cada atividade
possui código (
            string UUID, gerado pela aplicação),
            usuário,
            categoria de atividade (caminhada, ciclismo, corrida e natação),
            distância (em Km),
            duração (em minutos) e
            data da atividade.
*/
