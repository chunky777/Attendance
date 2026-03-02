package com.example.demo;
import org.springframework.web.bind.annotation.*;
import java.util.Random;


@RestController
public class GameController {
    private int answer;

    public GameController() {
        Random random = new Random();
        answer = random.nextInt(100) + 1; // 1から100までのランダムな数字を生成
        System.out.println("答えは：" + answer);
    }

    @GetMapping("/game")
    public String showGame() {
        return """
                <h1>最強ゲーム！！</h1>
                <form action="/check"method="post">
                 数字を入力: <input type="number" name="number">
                    <button type="submit">送信</button>
                </form>
                """;
    }

    @PostMapping("/check")
    public String checkAnswer(@RequestParam int number) {
        if (number < answer) {
            return "もっと大きい数字です！！";
        } else if (number > answer) {
            return "もっと小さい数字です！！";
        } else {
            //新しい数字を作る
            Random random = new Random();
            answer = random.nextInt(100) + 1; // 1から100までのランダムな数字を生成
            System.out.println("新しい答えは：" + answer);
            return "正解です！おめでとう！<br><a href='/game'>もう一回</a>";
        }
    }
    
    
}
