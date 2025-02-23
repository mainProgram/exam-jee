import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formatStatus',
  standalone: true
})
export class FormatStatusPipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    return null;
  }

}
