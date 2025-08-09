export interface Exercise {
  id: string;
  name: string;
  sets: string;
  reps: string;
  weight: string;
  restTime: number;
  notes?: string;
  workoutId: string;
  completed?: boolean;
}
