import { HttpException, Injectable } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import LoginDto from 'src/domain/dto/login.dto';
import RegisterDto from 'src/domain/dto/register.dto';
import { User } from 'src/domain/entity/user.entity';
import { UserService } from './user.service';

@Injectable()
export class AuthService {
  constructor(
    private readonly userService: UserService,
    private readonly jwtService: JwtService,
  ) { }

  async validateUser(payload: any, callback: Function): Promise<any> {
    const user = await this.userService.findOneByMail(payload.username);
    if (user && user.password === payload.sub) {
      const { password, ...result } = user;
      return result;
    }
    return null;
  }

  async login(user: LoginDto) {
    // check if user exists
    const userEntity = await this.userService.findOneByMail(user.mail);
    if (!userEntity) {
      throw new HttpException('Unauthorized', 401);
    }
    const payload = { username: user.mail, sub: user.password };
    // check if password is correct
    if (userEntity.password !== user.password) {
      throw new HttpException('Bad Credentials', 500);
    }
    return this.jwtService.sign(payload);
  }

  async register(body: RegisterDto): Promise<User> {

    // create user
    const user = new User();
    user.firstName = body.name;
    user.lastName = body.surname;
    user.mail = body.mail;

    // TODO: hash password
    user.password = body.password;

    return await this.userService.save(user);
  }
}