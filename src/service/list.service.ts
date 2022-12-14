import { HttpException, Injectable, UseFilters } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { HttpExceptionFilter } from 'src/core/http.exception.filter';
import CreateListDto from 'src/domain/dto/create.list.dto';
import { List } from 'src/domain/entity/list.entity';
import { Repository } from 'typeorm';

@Injectable()
export class ListService {
  constructor(
    @InjectRepository(List)
    private listRepository: Repository<List>) { }

  createList(userId: number, body: CreateListDto) {
    const list = new List();
    list.name = body.name;
    list.user = userId;
    this.listRepository.save(list);
  }

  async updateList(userId: number, listId: number, body: CreateListDto) {
    const response = await this.listRepository.findOneBy({ id: listId });

    if (response === null) return "List not found";
    if (response.user !== userId) throw new Error('You are not allowed to edit this list');


    if (body.name)
      response.name = body.name;

    return this.listRepository.save(response);
  }

  async deleteList(listId: number) {
    const list = await this.listRepository.findOneBy({ id: listId });

    if (!list) {
      throw new HttpException('List not found', 404);
    }

    return this.listRepository.delete({ id: listId });
  }

  async getList(id: number): Promise<List> {

    const result = this.listRepository.createQueryBuilder("list")
      .innerJoinAndSelect("list.tasks", "task")
      .where("list.id = :id", { id: id })
      .getOne();

    return result;
  }

  async save(list: List): Promise<List> {
    return this.listRepository.save(list);
  }

  async findById(id: number): Promise<List> {
    return this.listRepository.findOneBy({
      id: id
    });
  }

  async getLists(id: number) {

    const result = await this.listRepository.createQueryBuilder("list")
      .where("list.user = :id", {
        id
      }).getMany();

    return result;
  }

  async countOfLists(userId: number) {
    const count = await this.listRepository.count({
      where: { user: userId }
    })
    return count
  }

}



