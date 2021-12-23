package github.lyl21.wanandroid.bean

import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
data class UserDataInfo(
    val coinInfo: CoinInfo ,
    val userInfo: UserInfo ,
)


