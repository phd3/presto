/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.operator.scalar.timestamptz;

import io.trino.spi.function.LiteralParameter;
import io.trino.spi.function.LiteralParameters;
import io.trino.spi.function.ScalarFunction;
import io.trino.spi.function.SqlType;
import io.trino.spi.type.LongTimestampWithTimeZone;
import io.trino.spi.type.StandardTypes;

import static io.trino.spi.type.DateTimeEncoding.unpackMillisUtc;
import static io.trino.type.DateTimes.MICROSECONDS_PER_SECOND;
import static io.trino.type.DateTimes.MILLISECONDS_PER_SECOND;
import static io.trino.type.DateTimes.PICOSECONDS_PER_SECOND;

@ScalarFunction("to_unixtime")
public final class ToUnixTime
{
    private ToUnixTime() {}

    @LiteralParameters("p")
    @SqlType(StandardTypes.DOUBLE)
    public static double toUnixTime(@LiteralParameter("p") long precision, @SqlType("timestamp(p) with time zone") long timestamp)
    {
        long epochMillis = unpackMillisUtc(timestamp);
        if (precision <= 3) {
            return epochMillis * 1.0 / MILLISECONDS_PER_SECOND;
        }

        return epochMillis * 1.0 / MICROSECONDS_PER_SECOND;
    }

    @LiteralParameters("p")
    @SqlType(StandardTypes.DOUBLE)
    public static double toUnixTime(@SqlType("timestamp(p) with time zone") LongTimestampWithTimeZone timestamp)
    {
        return timestamp.getEpochMillis() * 1.0 / MILLISECONDS_PER_SECOND + timestamp.getPicosOfMilli() * 1.0 / PICOSECONDS_PER_SECOND;
    }
}
